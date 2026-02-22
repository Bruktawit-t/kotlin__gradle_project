package org.dbu.library.service

import org.dbu.library.model.Book
import org.dbu.library.repository.LibraryRepository

class DefaultLibraryService(
    private val repository: LibraryRepository
) : LibraryService {

    override fun addBook(book: Book): Boolean {
        return repository.addBook(book)
    }

    override fun borrowBook(patronId: String, isbn: String): BorrowResult {
        val book = repository.findBook(isbn)
            ?: return BorrowResult.BOOK_NOT_FOUND

        val patron = repository.findPatron(patronId)
            ?: return BorrowResult.PATRON_NOT_FOUND

        if (!book.isAvailable) return BorrowResult.BOOK_NOT_AVAILABLE
        if (patron.borrowedBooks.size >= 3) return BorrowResult.BORROW_LIMIT_REACHED

        book.isAvailable = false
        patron.borrowedBooks.add(book)

        repository.updateBook(book)
        repository.updatePatron(patron)

        return BorrowResult.SUCCESS
    }

    override fun returnBook(patronId: String, isbn: String): Boolean {
        val book = repository.findBook(isbn) ?: return false
        val patron = repository.findPatron(patronId) ?: return false

        val borrowedBook = patron.borrowedBooks.find { it.isbn == isbn }
            ?: return false

        borrowedBook.isAvailable = true
        patron.borrowedBooks.remove(borrowedBook)

        repository.updateBook(borrowedBook)
        repository.updatePatron(patron)

        return true
    }

    override fun search(query: String): List<Book> {
        return repository.getAllBooks().filter {
            it.title.contains(query, ignoreCase = true) ||
            it.author.contains(query, ignoreCase = true)
        }
    }
}