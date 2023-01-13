package chucknorris

data class DecodeException(override val message: String) : Exception()

fun String.encrypt(): String = mapNotNull {
    Integer.toBinaryString(it.code).padStart(7, '0')
}.joinToString(separator = "").let { string ->
    "(0+|1+)".toRegex().findAll(string).map { it.value }
}.map {
    "${if (it.startsWith('0')) "00" else "0"} ${"0".repeat(it.length)}"
}.joinToString(" ")

fun String.decrypt(): String = "(0+)".toRegex().findAll(this)
        .map { it.value }.chunked(2) {
            (if (it.first().length == 2) "0" else "1").repeat(it.last().length)
        }.joinToString(separator = "").chunked(7)
        .also {
            if (it.last().length != 7) throw DecodeException("Encoded string is not valid.")
        }
        .map {
            Integer.parseInt(it, 2).toChar()
        }.joinToString(separator = "")

fun String.isValidationForDecode(): Boolean = this.split("(0|00) 0+".toRegex()).all { it.isBlank() || it.isEmpty() }

fun main() {
    do {
        println("Please input operation (encode/decode/exit):")
        val action = readln()
        when (action) {
            "encode" -> {
                println("Input string:")
                val input = readln()

                println("Encoded string:")
                println(input.encrypt())
            }

            "decode" -> {
                println("Input encoded string:")
                val input = readln()

                if (input.isValidationForDecode()) {
                    try {
                        println(input.decrypt().also { println("Decoded string:") })
                    } catch (ex: Exception) {
                        println(ex.message)
                    }
                } else {
                    println("Encoded string is not valid.")
                }
            }

            "exit" -> {
                println("Bye!")
            }

            else -> {
                println("There is no '$action' operation")
            }
        }
    } while (action != "exit")
}
