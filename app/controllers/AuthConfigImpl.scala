package controllers

import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect.{ ClassTag, classTag }
import ExecutionContext.Implicits.global
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import jp.t2v.lab.play2.auth.{ AuthConfig, CookieTokenAccessor }

trait AuthConfigImpl extends AuthConfig {

  /**
   * ユーザを識別するIDの型です。String や Int や Long などが使われるでしょう。
   */
  type Id = String

  /**
   * あなたのアプリケーションで認証するユーザを表す型です。
   * User型やAccount型など、アプリケーションに応じて設定してください。
   */
  type User = models.User

  /**
   * 認可(権限チェック)を行う際に、アクション毎に設定するオブジェクトの型です。
   */
  type Authority = models.Role

  /**
   * CacheからユーザIDを取り出すための ClassTag です。
   * 基本的にはこの例と同じ記述をして下さい。
   */
  val idTag: ClassTag[Id] = classTag[Id]

  /**
   * セッションタイムアウトの時間(秒)です。
   */
  val sessionTimeoutInSeconds: Int = 3600

  /**
   * ユーザIDからUserブジェクトを取得するアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def resolveUser(id: Id)(implicit ctx: ExecutionContext): Future[Option[User]] =
    Future.successful(Some(models.User(id, "", models.Administrator)))

  /**
   * ログインが成功した際に遷移する先を指定します。
   */
  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Index.main))

  /**
   * ログアウトが成功した際に遷移する先を指定します。
   */
  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Application.login))

  /**
   * 認証が失敗した場合に遷移する先を指定します。
   */
  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext): Future[Result] =
    Future.successful(Redirect(routes.Application.login))

  /**
   * 認可(権限チェック)が失敗した場合に遷移する先を指定します。
   */
  override def authorizationFailed(request: RequestHeader, user: User, authority: Option[Authority])(implicit context: ExecutionContext): Future[Result] = {
    Future.successful(Forbidden("no permission"))
  }

  /**
   * 権限チェックのアルゴリズムを指定します。
   * 任意の処理を記述してください。
   */
  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext): Future[Boolean] = Future.successful {
    (user.role, authority) match {
      case (models.Administrator, _) => true
      case _                         => false
    }
  }

  /**
   * (Optional)
   * SessionID Tokenの保存場所の設定です。
   * デフォルトでは Cookie を使用します。
   */
  override lazy val tokenAccessor = new CookieTokenAccessor(
    /*
     * cookie の secureオプションを使うかどうかの設定です。
     * デフォルトでは利便性のために false になっていますが、
     * 実際のアプリケーションでは true にすることを強く推奨します。
     */
    cookieSecureOption = play.api.Play.isProd(play.api.Play.current),
    cookieMaxAge = Some(sessionTimeoutInSeconds)
  )

}
