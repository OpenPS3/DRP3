package net.ps3utils.discord.rpc

import PS3InfoFetcher
import java.util.Scanner

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    print("Insert your PS3 ID: 192.168.")
    val ip = "192.168." + scanner.nextLine()
    PS3InfoFetcher(ip).startLoop(30000)
}
