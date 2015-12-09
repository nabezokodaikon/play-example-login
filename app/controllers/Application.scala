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
    // val filledForm = loginForm.fill(User("E-mail Address", "Password"))

    val b = loginForm.bind(Map("email" -> "example.com", "password" -> "pass"))
    println(b.toString)

    Ok(views.html.login("hello"))
  }

  def auth() = Action { implicit request =>

    val b = request.body
    println(b.toString)



    loginForm.bindFromRequest.fold(
      errors => {
        println(errors)
        BadRequest(errors.toString)
      },
      success => {
        println("Success!")
        Ok(success.toString)
      }
    )
  }

}
