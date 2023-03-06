package com.laundrybuoy.customer.utils

import java.io.IOException

class NoInternetException(message : String) : IOException(message)
class APIException(message : String) : IOException(message)