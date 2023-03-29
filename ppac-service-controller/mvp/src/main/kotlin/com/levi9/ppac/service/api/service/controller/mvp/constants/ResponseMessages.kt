package com.levi9.ppac.service.api.service.controller.mvp.constants

object ResponseMessages {

    const val OK = "Your request was successful."
    const val NO_CONTENT = "Your request was successful, but there is no content to return."
    const val BAD_REQUEST_MESSAGE = "Your request was invalid. Please check your parameters"
    const val MISSING_HEADER =
        "The request is missing the required header. Please ensure that you include the header in your request header and try again."
    const val UNAUTHORIZED_MESSAGE = "You are not authorized to access this resource."
    const val NOT_FOUND_MESSAGE = "The resource you requested could not be found."
    const val CONFLICT_MESSAGE =
        "The resource already exists or there is a conflict with the current state of the resource."
    const val METHOD_NOT_ALLOWED_MESSAGE = "The HTTP method you used is not supported for this resource."
    const val TIMEOUT_MESSAGE =
        "The server did not receive a complete request from the client within the expected time frame."
    const val INTERNAL_SERVER_ERROR_MESSAGE = "An unexpected error occurred on the server. Please try again later."
    const val INVALID_PARAMETER = "Invalid parameter: "

}
