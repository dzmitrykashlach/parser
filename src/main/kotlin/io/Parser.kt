package io

interface Parser<T> {
    fun tryParse():T?
}