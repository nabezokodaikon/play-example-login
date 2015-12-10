package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import jp.t2v.lab.play2.auth.AuthElement
import models._

class Index extends Controller with AuthElement with AuthConfigImpl {

  def main() = StackAction(AuthorityKey -> Administrator) { implicit request =>
    val user = loggedIn
    Ok(views.html.main(user))
  }
}
