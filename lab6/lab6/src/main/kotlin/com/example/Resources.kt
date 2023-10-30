package com.example

import com.example.Post.postText
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.Resources
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

fun Application.configureResources() {
    install(Resources)
    routing{
        get<Posts> {
            //handler for /posts
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .selectAll()
                        .map{PostData(it[postText], it[Post.timeStamp], it[Post.id].value)}
                }
            )
        }

        post<Posts> {
            val postData = call.receive<GetPostData>()
            newSuspendedTransaction (Dispatchers.IO, MyDatabase.db )  {
                Post.insert{
                    it[postText] = postData.value
                    it[timeStamp] = System.currentTimeMillis()
                } get Post.id
            }
            call.respondText("Posted...$postData")
        }

        //Get specific post using ID
        get<Posts.GetID> {
            call.respond(
                newSuspendedTransaction(Dispatchers.IO) {
                    Post
                        .select(Post.id eq it.id)
                        .map { PostData(it[postText], it[Post.timeStamp], it[Post.id].value ) }
                }
            )
        }

        // get post based on timestamp
        get<Posts.GetTimeStamp> {
            call.respond(
                newSuspendedTransaction ( Dispatchers.IO)  {
                    Post
                        .select(Post.timeStamp greaterEq it.timestamp)
                        .map { PostData(it[postText], it[Post.timeStamp], it[Post.id].value ) }
                }
            )
        }

        // delete by post id
        delete<Posts.DeleteID> {
            val id = it.id
            val deletedRows = newSuspendedTransaction(Dispatchers.IO) {
                Post.deleteWhere {Post.id eq id}
            }

            if (deletedRows > 0) {
                call.respondText("$id has been deleted")
            } else{
                (call.respondText("$id NOT FOUND"))
            }
        }
    }
}

@Resource("/posts") //corresponds to /posts
class Posts {
    @Resource("{timestamp}/timestamp") //corresponds to /books/like?title=some%20book
    class GetTimeStamp(val parent: Posts = Posts(), val timestamp: Long)

    @Resource("{id}/id")
    class GetID(val parent: Posts = Posts(), val id: Int)

    @Resource("{id}/delete")
    class DeleteID(val parent: Posts = Posts(), val id: Int)
}
@Serializable data class PostData(val value: String, val timestamp: Long, val id: Int)
@Serializable data class GetPostData(val value: String)