package resources

import android.view.{View, ViewGroup}
import android.widget.BaseAdapter

/**
 * Created by christopher on 30/01/15.
 */
case class SongListAdapter() extends BaseAdapter {
  override def getCount: Int = ???

  override def getItemId(position: Int): Long = ???

  override def getView(position: Int, convertView: View, parent: ViewGroup): View = ???

  override def getItem(position: Int): AnyRef = ???
}
