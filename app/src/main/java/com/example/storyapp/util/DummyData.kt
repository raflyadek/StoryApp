package com.example.storyapp.util

import com.example.storyapp.data.remote.ListStoryResponse
import com.example.storyapp.data.remote.LoginResponse
import com.example.storyapp.data.remote.LoginResult
import com.example.storyapp.data.remote.RegisterResponse
import com.example.storyapp.data.remote.Story
import com.example.storyapp.data.remote.UploadResponse

object DummyData {

    fun generateDummyStoriess(): ListStoryResponse {
        val listStory = ArrayList<Story>()
        for (i in 1..20) {
            val story = Story(
                createdAt = "2022-02-22T22:22:22Z",
                description = "Description $i",
                id = "id_$i",
                lat = i.toDouble() * 10,
                lon = i.toDouble() * 10,
                name = "Name $i",
                photoUrl = "https://akcdn.detik.net.id/visual/2020/02/14/066810fd-b6a9-451d-a7ff-11876abf22e2_169.jpeg?w=650"
            )
            listStory.add(story)
        }

        return ListStoryResponse(
            error = false,
            message = "Stories fetched successfully",
            listStory = listStory
        )
    }

    fun generateDummyStories(): ListStoryResponse {
        val listStory = ArrayList<Story>()
        for (i in 1..10) {
            val story = Story(
                createdAt = "2022-02-22",
                description = "description",
                id = "id",
                lat = 12312.1,
                lon = 1231.1,
                name = "Name",
                photoUrl = "https://i.pinimg.com/736x/d1/dc/42/d1dc427714619b8f774802c348d83117.jpg"
            )
            listStory.add(story)
        }

        return ListStoryResponse(
            error = false,
            message = "success",
            listStory = listStory
        )
    }

    fun generateUploadStoryResponse(): UploadResponse {
        return UploadResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyRegisterResponse(): RegisterResponse {
        return RegisterResponse(
            error = false,
            message = "success"
        )
    }

    fun generateDummyLoginResponse(): LoginResponse {
        return LoginResponse(
            error = false,
            message = "success",
            loginResult = LoginResult(
                userId = "asdf",
                name = "Rafly ade k",
                token = "asdfwersdfasdf"
            )
        )
    }
}
