import diff.JsonDiffController
import diff.myers.MyersDiff

trait DiffModule {

  import com.softwaremill.macwire.MacwireMacros._

  lazy val jsonDiffController = wire[JsonDiffController]
  lazy val differ = wire[MyersDiff]
}

object Application extends DiffModule
