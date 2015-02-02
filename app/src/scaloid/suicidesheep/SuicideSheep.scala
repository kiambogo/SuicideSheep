package kiambogo.suicidesheep

import org.scaloid.common._
import android.graphics.Color

class SuicideSheep extends SActivity {

  onCreate {
    contentView = new SVerticalLayout {
      style {
        case b: SButton => b.textColor(Color.RED).onClick(toast("Bang!"))
        case t: STextView => t textSize 10.dip
        case e: SEditText => e.backgroundColor(Color.YELLOW)
      }
      STextView("Me too")
      SImageView(R.drawable.ic_launcher_web)
      STextView("I am 15 dip tall") textSize 15.dip // overriding
      this += new SLinearLayout {
        STextView("Button: ")
        SButton(R.string.red)
      }.wrap
      SEditText("Yellow input field fills the space").fill
    } padding 20.dip
  }

}
