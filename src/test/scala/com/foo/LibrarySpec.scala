package com.foo

import org.scalatest._

class LibrarySpec
  extends WordSpec
  with MustMatchers
  with TestData {

  "Should be able to add a book to the library catalog" in {
    PhiladelphiaLibrary.addToCatalog(
      SorcerersStoneId,
      author = JKRowling,
      published = SorcerersStonePublished,
      isbn = SorcerersStoneIsbn,
      title = SorcerersStoneTitle,
      cover = HardCover,
      pages = SorcerersStonePages,
      description = SorcerersStoneDesc
    ).catalog must be(Seq(SorcerersStone))
  }

  "Should be able to remove a book from the library catalog" in {
    val lib = PhiladelphiaLibrary.copy(catalog = Seq(SorcerersStone, DeathlyHollows))

    lib.catalog must be(Seq(SorcerersStone, DeathlyHollows))
    lib.removeFromCatalog(SorcerersStone.id).catalog must be(Seq(DeathlyHollows))
  }

  "Should be able to add copies of a book to a library" in {
    val libWithTwoCopies = PhiladelphiaLibrary.copy(catalog = Seq(SorcerersStone))
      .addCopy(SorcerersStone.isbn, SorcerersStoneCopyOneBarcode)
      .addCopy(SorcerersStone.isbn, SorcerersStoneCopyTwoBarcode)

    libWithTwoCopies.findBookByIsbn(SorcerersStone.isbn).get.copies must be (TwoCopiesOfSorcerersStoneNoneCheckedOut)
  }

  "Should be able to remove a copy of a book from the library" in {
    val libWithRemovedCopy = LibraryWithTwoCopiesOfSorcerersStoneNoneCheckedOut.removeCopy(SorcerersStone.isbn, SorcerersStoneCopyOneBarcode)
    libWithRemovedCopy.findBookByIsbn(SorcerersStone.isbn).get.copies must be (Seq(BookCopy(SorcerersStoneCopyTwoBarcode)))
  }

  "Should be able to check out a copy of a book from the library" in {
    val libWithCheckeOutCopy = LibraryWithTwoCopiesOfSorcerersStoneNoneCheckedOut.checkoutCopy(SorcerersStone.isbn, SorcerersStoneCopyOneBarcode, LibraryMember.id)
    libWithCheckeOutCopy.findBookByIsbn(SorcerersStone.isbn).get.copies.sortBy(_.barcode) must be (TwoCopiesOfSorcerersStoneOneCheckedOut)
  }

  "Should be able to check in a copy of a book from the library" in {
    val libWithNoneCheckedOut = LibraryWithTwoCopiesOfSorcerersStoneOneCheckedOut.checkinCopy(SorcerersStone.isbn, SorcerersStoneCopyOneBarcode)
    libWithNoneCheckedOut.findBookByIsbn(SorcerersStone.isbn).get.copies.sortBy(_.barcode) must be(TwoCopiesOfSorcerersStoneNoneCheckedOut)
  }

}
