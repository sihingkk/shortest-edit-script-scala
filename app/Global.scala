import com.softwaremill.macwire.Macwire
import play.api.GlobalSettings
import play.api.mvc.WithFilters

object Global extends WithFilters(new LoggingFilter) with Macwire {
  val wired = wiredInModule(Application)

  override def getControllerInstance[A](controllerClass: Class[A]) = wired.lookupSingleOrThrow(controllerClass)
}
