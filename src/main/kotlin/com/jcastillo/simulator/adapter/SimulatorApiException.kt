package com.jcastillo.simulator.adapter

class EmptyFileException(message: String) : RuntimeException(message)
class InvalidFileFormatException(message: String) : RuntimeException(message)
class EmptyBodyException(message: String) : RuntimeException(message)
class NegativeNumberOfDaysException(message: String) : RuntimeException(message)