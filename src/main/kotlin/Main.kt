package org.example

import kotlin.random.Random
import kotlin.system.exitProcess

data class Solution(val assignment: List<Int>, val maxLoad: Int)

fun parseArguments(args: Array<String>): IntArray {
    check(args.isNotEmpty()) {
        "No arguments given!"
    }

    val argumentCount = 5
    check(args.count() == argumentCount) {
        "Wrong number of arguments"
    }

    return intArrayOf(
        args[0].toInt(),
        args[1].toInt(),
        args[2].toInt(),
        args[3].toInt(),
        args[4].toInt()
    )
}

fun generateInitialSolution(tasks: List<Int>, numProcessors: Int): Solution {
    val assignment = tasks.map { Random.nextInt(numProcessors) }
    return evaluateSolution(tasks, assignment, numProcessors)
}

fun evaluateSolution(tasks: List<Int>, assignment: List<Int>, numProcessors: Int): Solution {
    val loads = IntArray(numProcessors) { 0 }
    for (i in tasks.indices) {
        loads[assignment[i]] += tasks[i]
    }
    return Solution(assignment, loads.maxOrNull() ?: 0)
}

fun mutateSolution(solution: Solution, numProcessors: Int): List<Int> {
    val newAssignment: MutableList<Int> = solution.assignment.toMutableList()
    val taskToMutate = Random.nextInt(newAssignment.size)
    newAssignment[taskToMutate] = Random.nextInt(numProcessors)
    return newAssignment
}

fun optimize(tasks: List<Int>, numProcessors: Int, iterations: Int): Solution {
    var currentSolution = generateInitialSolution(tasks, numProcessors)
    for (i in 1..iterations) {
        val newAssignment = mutateSolution(currentSolution, numProcessors)
        val newSolution = evaluateSolution(tasks, newAssignment, numProcessors)
        if (newSolution.maxLoad < currentSolution.maxLoad) {
            currentSolution = newSolution
        }
    }
    return currentSolution
}

fun main(args: Array<String>) {
    val parsedArguments = try {
        parseArguments(args)
    }
    catch (e: IllegalStateException) {
        println(e.message)
        exitProcess(-1)
    }

    val taskCount = parsedArguments[0]
    val minTaskSize = parsedArguments[1]
    val maxTaskSize = parsedArguments[2]
    val numProcessors = parsedArguments[3]
    val iterations = parsedArguments[4]

    println("Given Arguments")
    println("Task count:        $taskCount")
    println("Min task size:     $minTaskSize")
    println("Max task size:     $maxTaskSize")
    println("Number Processors: $numProcessors")
    println("Number iterations: $iterations")

    val tasks = List(taskCount) { Random.nextInt(minTaskSize, maxTaskSize) }
    println("")
    println("Generated tasks: $tasks")

    val optimizedSolution = optimize(tasks, numProcessors, iterations)
    println("")
    println("Final result")
    println("Optimized Assignment: ${optimizedSolution.assignment}")
    println("Max Load: ${optimizedSolution.maxLoad}")
}