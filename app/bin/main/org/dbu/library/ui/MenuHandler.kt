package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {
        "1" -> { addBook(service); true }
        "2" -> { registerPatron(repository); true }
        "3" -> { borrowBook(service); true }
        "4" -> { returnBook(service); true }
        "5" -> { search(service); true }
        "6" -> { listAllBooks(repository); true }
        "0" -> false
        else -> {
            println("Invalid option")
            true
        }
    }
}

fun addBook(service: LibraryService) {
    print("Enter ISBN: ")
    val isbn = readLine()!!
    print("Enter title: ")
    val title = readLine()!!
    print("Enter author: ")
    val author = readLine()!!

    val book = Book(isbn, title, author, false)
    if (service.addBook(book)) {
        println("Book added successfully")
    } else {
        println("Book already exists")
    }
}

fun registerPatron(repository: LibraryRepository) {
    print("Enter Patron ID: ")
    val id = readLine()!!
    print("Enter Patron Name: ")
    val name = readLine()!!

    val patron = Patron(id, name)
    if (repository.addPatron(patron)) {
        println("Patron registered successfully")
    } else {
        println("Patron already exists")
    }
}

fun borrowBook(service: LibraryService) {
    print("Enter Patron ID: ")
    val patronId = readLine()!!
    print("Enter Book ISBN: ")
    val isbn = readLine()!!

    when (service.borrowBook(patronId, isbn)) {
        BorrowResult.Success -> println("Book borrowed successfully")
        BorrowResult.BookNotFound -> println("Book not found")
        BorrowResult.PatronNotFound -> println("Patron not found")
        BorrowResult.BookAlreadyBorrowed -> println("Book already borrowed")
    }
}

fun returnBook(service: LibraryService) {
    print("Enter Patron ID: ")
    val patronId = readLine()!!
    print("Enter Book ISBN: ")
    val isbn = readLine()!!

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully")
    } else {
        println("Return failed")
    }
}

fun search(service: LibraryService) {
    print("Enter search query: ")
    val query = readLine()!!
    val results = service.search(query)

    if (results.isEmpty()) {
        println("No books found")
    } else {
        results.forEach { println(it) }
    }
}

fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("No books available")
    } else {
        books.forEach { println(it) }
    }
}