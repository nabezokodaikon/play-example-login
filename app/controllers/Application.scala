package controllers

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import jp.t2v.lab.play2.auth.LoginLogout
import models._

class Application extends Controller with LoginLogout with AuthConfigImpl {

  val loginForm = Form(
    mapping(
      "email" -> email,
      "password" -> text
    )(User.authenticate)(_.map(user => (user.email, "")))
      .verifying("Invalid email or password", result => result.isDefined)
  )

  def login() = Action {
    Ok(views.html.login())
  }

  def logout() = Action.async { implicit request =>
    gotoLogoutSucceeded
  }

  def authenticate = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.login()))
      },
      user => {
        gotoLoginSucceeded(user.get.email)
      }
    )
  }

}
