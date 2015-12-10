package models

case class User(email: String, password: String, role: Role)

object User {

  def authenticate(email: String, password: String): Option[User] = {
    if (email == """example@gmail.com""" && password == """password""") {
      Some(User(email, password, Administrator))
    } else {
      None
    }
  }
}
