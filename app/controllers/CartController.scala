package controllers

import javax.inject._
import play.api.mvc._
import services.CartService
import models.Cart
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext

@Singleton
class CartController @Inject()(cc: ControllerComponents, cartService: CartService)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def listCart: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val cartData = request.body.asFormUrlEncoded.get
    val userID = cartData("userID").head.toLong
    cartService.allCarts(userID.toString().toLong).map { carts =>
      Ok(Json.toJson(carts))
    }
  }

  def addCart: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val cartData = request.body.asFormUrlEncoded.get
    val userID = cartData("userID").head.toLong
    val apparelID = cartData("apparelID").head.toLong
    val quantity = cartData("quantity").head.toInt
    val price = cartData("price").head.toInt
    val cart = Cart(None, userID, apparelID, quantity ,price)
    cartService.addCart(cart).map(_ =>
      Created(Json.toJson(cart))
    )
  }

  def deleteCart: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    val cartData = request.body.asFormUrlEncoded.get
    val userID = cartData("userID").head.toLong
    val apparelID = cartData("apparelID").head.toLong

    cartService.deleteCart(apparelID, userID).map { result =>
        Ok(s"Delete successful")

    }
  }



}