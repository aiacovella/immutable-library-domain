package com.foo

import java.util.UUID
import org.joda.time.LocalDate

/**
 * Represents a definition of a book
 * @param isbn unique identifier of the book
 * @param author author of the book
 * @param published copy write year
 * @param title book title
 * @param cover book cover type
 * @param description brief description of the book
 * @param pages number of pages in the book
 * @param copies instances of the book
 */
case class Book(
  id: UUID,
  author: Author,
  published: LocalDate,
  isbn: String,
  title: String,
  cover: Cover,
  description: String,
  pages: Int,
  copies: Seq[BookCopy] = Seq()) {

  def addCopy(barcode: String) = copy(copies = copies :+ BookCopy(barcode))

  def removeCopy(barcode: String) = copy(copies = copies.filterNot(copyToRemove => copyToRemove.barcode == barcode))

  def checkoutCopy(memberId: UUID, barcode: String) = {
    copies.find(bookCopy => bookCopy.barcode == barcode).fold {
      this
    } { existingCopy =>
      copy(copies = copies.filterNot(bookCopy => bookCopy.barcode == existingCopy.barcode) :+ existingCopy.copy(possessedBy = Some(memberId)))
    }
  }

  def checkinCopy(barcode: String) = {
    copies.find(bookCopy => bookCopy.barcode == barcode).fold {
      this
    } { existingCopy =>
      copy(copies = copies.filterNot(bookCopy => bookCopy.barcode == existingCopy.barcode) :+ existingCopy.copy(possessedBy = None))
    }
  }
}

/**
 * Represents an instance of a book
 * @param barcode barcode for the unique instance of the book
 * @param possessedBy either a library member or None if possessed by the library
 */
case class BookCopy(barcode: String, possessedBy: Option[UUID] = None) {

  def checkOut(memberId: UUID) = copy(possessedBy = Some(memberId))

  def checkIn() = copy(possessedBy = None)

}

case class Library(
                    id: UUID,
                    name: String,
                    address: String,
                    city: String,
                    state: String,
                    zipCode: String,
                    catalog: Seq[Book] = Seq()) {

  def addToCatalog(id: UUID,
    author: Author,
    published: LocalDate,
    isbn: String,
    title: String,
    cover: Cover,
    description: String,
    pages: Int) = {

    val newBook = Book(id, author, published, isbn, title, cover, description, pages)
    copy(catalog = catalog :+ newBook)
  }

  def removeFromCatalog(bookId: UUID) = copy(catalog = catalog.filterNot(bk => bk.id == bookId))

  def addCopy(isbn: String, barcode: String) = {

    catalog.find(book => book.isbn == isbn).fold {
      this
    } { existingBook =>
      copy(catalog = catalog.filterNot(bk => bk.id == existingBook.id) :+ existingBook.addCopy(barcode))
    }
  }

  def removeCopy(isbn: String, barcode: String) = {
    catalog.find(book => book.isbn == isbn).fold {
      this
    } {
      existingBook => copy(catalog = catalog.filterNot(bk => bk.id == existingBook.id) :+ existingBook.removeCopy(barcode))
    }
  }

  def checkoutCopy(isbn: String, barcode: String, memberId: UUID) = {
    catalog.find(book => book.isbn == isbn).fold {
      this
    } { existingBook =>
      copy(catalog = catalog.filterNot(bk => bk.id == existingBook.id) :+ existingBook.checkoutCopy(memberId, barcode))
    }
  }

  def checkinCopy(isbn: String, barcode: String) = {
    catalog.find(book => book.isbn == isbn).fold {
      this
    } { existingBook =>
      copy(catalog = catalog.filterNot(bk => bk.id == existingBook.id) :+ existingBook.checkinCopy(barcode))
    }
  }

  def findBookByIsbn(isbn: String) = catalog.find(bk => bk.isbn == isbn)

}

// Person types
sealed trait Person {
  def id: UUID

  def firstName: String

  def middleInitial: Option[Char]

  def lastName: String

  def birthDate: LocalDate
}

case class Author(
   id: UUID,
   firstName: String,
   middleInitial: Option[Char],
   lastName: String,
   birthDate: LocalDate
 ) extends Person


case class Member(
  id: UUID,
  firstName: String,
  middleInitial: Option[Char],
  lastName: String,
  address: String,
  city: String,
  state: String,
  zipCode: String,
  birthDate: LocalDate
) extends Person


// Book types
sealed trait Cover
case object HardCover extends Cover
case object SoftCover extends Cover


