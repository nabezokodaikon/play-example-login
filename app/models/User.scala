package models

case class User(email: String, password: String, role: Role)

object User {

  def authenticate(email: String, password: String): Option[User] = {
    if (email == """alienware.seven@gmail.com""" && password == """shinkuhadoken""") {
      Some(User(email, password, Administrator))
    } else {
      None
    }
  }
}
