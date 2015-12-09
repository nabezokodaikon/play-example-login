package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import models.User

class Application extends Controller {

  val loginForm = Form(
    mapping(
      "email" -> text,
      "password" -> text
    )(User.apply)(User.unapply)
  )

  def login() = Action {
    Ok(views.html.login())
  }

  def auth() = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      errors => {
        BadRequest(errors.toString)
      },
      success => {
        Ok(success.toString)
      }
    )
  }

}
