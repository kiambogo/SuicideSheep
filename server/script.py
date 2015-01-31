import os
import pprint
import sys
import json
import requests
import sqlite3
from boto.s3.connection import S3Connection
from boto.s3.key import Key
import youtube_dl
import yaml

with open("credentials.yml") as cred_json:
  cred = yaml.load(cred_json)

AWS_ACCESS_KEY = cred['aws_access_key'] 
AWS_SECRET_KEY = cred['aws_secret_key'] 
YOUTUBE_API_KEY = cred['google_youtube_key'] 

URL = "https://gdata.youtube.com/feeds/api/users/MrSuicideSheep/uploads?&alt=json&max-results=50&key="+YOUTUBE_API_KEY+"&start-index="

s3 = S3Connection(AWS_ACCESS_KEY, AWS_SECRET_KEY) 
bucket = s3.get_bucket('suicide-sheep')
sql = sqlite3.connect('./suicidesheep')
sql.text_factory = str
cur = sql.cursor()

def get_next_id():
	next_id_cur = cur.execute('SELECT * from sqlite_sequence where name= \"songs\"')
	next_id = next_id_cur.fetchone()
	return int(next_id[1]) + 1

index = 1
while True:
	resp = requests.get(URL+str(index))
	jsonObj = json.loads(resp.content)
	if 'entry' not in jsonObj['feed']:
		break
	else: 
		for item in jsonObj['feed']['entry']:
			duration = item['media$group']['media$content'][0]['duration']
			if duration < 600:
				split_title = item['title']['$t'].split(' - ')
				name = split_title[0] 
				artist = split_title[1]
				uploadDate = item['published']['$t']
				duration = item['media$group']['media$content'][0]['duration']
				viewCount = item['yt$statistics']['viewCount']
				thumbnail_url = item['media$group']['media$thumbnail'][3]['url']
				large_url = item['media$group']['media$thumbnail'][0]['url']
				db_entry = ("\""+name+"\"", artist, duration, uploadDate, viewCount, 0, 0, '')
			else:
				name = item['title']['$t'] 
				artist = "SuicideSheep" 
				uploadDate = item['published']['$t']
				duration = item['media$group']['media$content'][0]['duration']
				viewCount = item['yt$statistics']['viewCount']
				thumbnail_url = item['media$group']['media$thumbnail'][3]['url']
				large_url = item['media$group']['media$thumbnail'][0]['url']
				db_entry = ("\""+name+"\"", artist, duration, uploadDate, viewCount, 1, 0, '')	
			image_set = (thumbnail_url, large_url)
			next_id = get_next_id()

			exists_cur = cur.execute('SELECT id from songs where artist = \"'+db_entry[1]+'\" and duration = '+str(db_entry[2])+'')			
			exists = exists_cur.fetchone()
			if exists == None:
				cur.execute('INSERT INTO songs (id, title, artist, duration, uploadDate, viewCount, isMix, isSheeep, s3_url) values (NULL, ?, ?, ?, ?, ?, ?, ?, ?);', db_entry)
				cur.execute('INSERT INTO images (id, thumbnail_url, large_url) values (NULL, ?, ?);', image_set)
			else:
				existing_id = int(exists[0])
				cur.execute('UPDATE songs SET viewCount = \"'+str(viewCount)+'\" where id = '+str(existing_id)+'')

			existing_cur = cur.execute('SELECT id, s3_url from songs where artist = \"'+db_entry[1]+'\" and duration = '+str(db_entry[2])+'')
			row = existing_cur.fetchone()
			if row[1] == '':
				ydl_opts = {
					'format': 'bestaudio/best',
					'outtmpl': name+":"+artist+".mp4" 
				}
				with youtube_dl.YoutubeDL(ydl_opts) as ydl:
					try: ydl.download([item['link'][0]['href']])
					except:
						print("Error downloading, skipping...")
						continue
					k = Key(bucket)
					k.key = str(row[0])+'.mp4' 
					k.set_contents_from_filename(name+":"+artist+".mp4")
					os.remove(name+":"+artist+".mp4")
				cur.execute('UPDATE songs SET s3_url = \"https://s3.amazonaws.com/suicide-sheep/'+str(row[0])+'.mp4\" where id = \"'+str(row[0])+'\"')
			sql.commit()
	index += 50
cur.close()
k = Key(bucket)
k.key = 'suicidesheep.db'
k.set_contents_from_filename('suicidesheep')
