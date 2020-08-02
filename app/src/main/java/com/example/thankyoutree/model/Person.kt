package com.example.thankyoutree.model

data class Person(var name: String, var count: Int) : Comparable<Person> {
    override fun compareTo(other: Person): Int {
        return other.count-count
    }
}

