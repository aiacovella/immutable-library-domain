package com.foo

import java.util.UUID

import org.joda.time.LocalDate

trait TestData {
  private[foo] val AuthorFirstName = "Joanne"
  private[foo] val AuthorMiddleInitial = Some('K')
  private[foo] val AuthorLastName = "Rowling"
  private[foo] val AuthorBirthDate = new LocalDate(1969, 3, 3)

  private[foo] val MemberFirstName = "James"
  private[foo] val MemberMiddleInitial = Some('R')
  private[foo] val MemberLastName = "Hoffa"
  private[foo] val MemberBirthDate = new LocalDate(1913, 2, 14)
  private[foo] val MemberAddress = "1414 Something Way"
  private[foo] val MemberCity = "Detroit"
  private[foo] val MemberState = "Michigan"
  private[foo] val MemberZipCode = "48210"

  private[foo] val LibraryName = "Philadelphia Free Library"
  private[foo] val LibraryAddress = "1414 Something Way"
  private[foo] val LibraryCity = "Philadelphia"
  private[foo] val LibraryState = "Pennsylvania"
  private[foo] val LibraryZipCode = "19115"

  private[foo] val SorcerersStoneCopyOneBarcode = "101-3211-1411"
  private[foo] val SorcerersStoneCopyTwoBarcode = "101-3211-1422"

  val SorcerersStoneId = nextId
  val SorcerersStoneIsbn = "978-0590353403"
  val SorcerersStoneTitle = "Harry Potter and the Sorcerer's Stone"
  val SorcerersStoneDesc = "Harry Potter has no idea how famous he is. That's because he's being raised by ..."
  val SorcerersStonePublished = new LocalDate(1997, 3, 30)
  val SorcerersStonePages = 320

  private[foo] val PhiladelphiaLibrary = Library(
    id = nextId,
    name = LibraryName,
    address = LibraryAddress,
    city = LibraryCity,
    state = LibraryState,
    zipCode = LibraryZipCode,
    catalog = Seq()
  )

  private[foo] val LibraryMember = Member(
    id = nextId,
    firstName = MemberFirstName,
    middleInitial = MemberMiddleInitial,
    lastName = MemberLastName,
    birthDate = MemberBirthDate,
    address = MemberAddress,
    city = MemberCity,
    state = MemberState,
    zipCode = MemberZipCode
  )

  private[foo] val JKRowling = Author(nextId, AuthorFirstName, AuthorMiddleInitial, AuthorLastName, AuthorBirthDate)

  private[foo] val SorcerersStone = {
    Book(
      id = SorcerersStoneId,
      author = JKRowling,
      published = SorcerersStonePublished,
      isbn = SorcerersStoneIsbn,
      title = SorcerersStoneTitle,
      cover = HardCover,
      pages = SorcerersStonePages,
      description = SorcerersStoneDesc
    )
  }

  private[foo] val DeathlyHollows = Book(
    id = nextId,
    author = JKRowling,
    published = new LocalDate(2009, 7, 7),
    isbn = " 978-0545139700",
    title = "Harry Potter And The Chamber Of Secrets",
    cover = SoftCover,
    pages = 784,
    description = "The brilliant, breathtaking conclusion to J.K. Rowling's spellbinding series is not for the faint of heart ..."
  )

  private[foo] def nextId = UUID.randomUUID()

  private[foo] val TwoCopiesOfSorcerersStoneNoneCheckedOut = Seq(BookCopy(SorcerersStoneCopyOneBarcode), BookCopy(SorcerersStoneCopyTwoBarcode))
  private[foo] val TwoCopiesOfSorcerersStoneOneCheckedOut = Seq(BookCopy(SorcerersStoneCopyOneBarcode, Some(LibraryMember.id)), BookCopy(SorcerersStoneCopyTwoBarcode))

  private[foo] val LibraryWithTwoCopiesOfSorcerersStoneNoneCheckedOut =
    PhiladelphiaLibrary.copy(catalog = Seq(SorcerersStone.copy(copies = TwoCopiesOfSorcerersStoneNoneCheckedOut)))

  private[foo] val LibraryWithTwoCopiesOfSorcerersStoneOneCheckedOut =
    PhiladelphiaLibrary.copy(catalog = Seq(SorcerersStone.copy(copies = TwoCopiesOfSorcerersStoneOneCheckedOut)))
}
