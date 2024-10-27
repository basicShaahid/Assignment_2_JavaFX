package util;

import model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository = BookRepository.getInstance();
        bookRepository.insertInitialBooks();  // Ensure the initial data is available for testing
    }

    @Test
    void insertInitialBooks() {
        //Arrange : this test has passesd last tested 10/27/2024
        List<Book> books = bookRepository.getAllBooks();
        assertTrue(books.size() >= 9, "The initial books should be inserted, with at least 9 books available.");
    }

    @Test
    void getAllBooks() {
        //Arrange : this test has passesd last tested 10/27/2024
        List<Book> books = bookRepository.getAllBooks();
        assertNotNull(books, "getAllBooks should return a non-null list.");
        assertTrue(books.size() > 0, "getAllBooks should return a list with books.");
    }

    @Test
    void getTop5Books() {
        //Arrange : this test has passesd last tested 10/27/2024
        List<Book> topBooks = bookRepository.getTop5Books();
        assertEquals(5, topBooks.size(), "getTop5Books should return exactly 5 books.");

        // Verify that the books are sorted by soldCopies in descending order
        for (int i = 0; i < topBooks.size() - 1; i++) {
            assertTrue(topBooks.get(i).getSoldCopies() >= topBooks.get(i + 1).getSoldCopies(),
                    "Books should be sorted by soldCopies in descending order.");
        }
    }

    @Test
    void addBook() {
        //Arrange : this test has passesd last tested 10/27/2024
        Book newBook = new Book(0, "Test Book", "Test Author", 10, 19.99, 0);
        bookRepository.addBook(newBook);

        // Verify the book was added
        List<Book> books = bookRepository.getAllBooks();
        assertTrue(books.stream().anyMatch(book -> "Test Book".equals(book.getTitle())),
                "The book should be added to the database.");
    }

    @Test
    void updateBook() {
        //Arrange : this test has passesd last tested 10/27/2024
        Book bookToUpdate = new Book(0, "Test Book", "Updated Author", 20, 25.99, 100);
        bookRepository.addBook(bookToUpdate);

        // Update book details
        bookToUpdate.setAuthor("Updated Author");
        bookToUpdate.setPhysicalCopies(15);
        bookToUpdate.setPrice(29.99);
        bookToUpdate.setSoldCopies(120);

        bookRepository.updateBook(bookToUpdate);

        // Fetch updated book and verify the changes
        List<Book> books = bookRepository.getAllBooks();
        Book updatedBook = books.stream().filter(book -> "Test Book".equals(book.getTitle())).findFirst().orElse(null);

        assertNotNull(updatedBook, "The updated book should exist in the database.");
        assertEquals("Updated Author", updatedBook.getAuthor(), "The author should be updated.");
        assertEquals(15, updatedBook.getPhysicalCopies(), "The physical copies should be updated.");
        assertEquals(29.99, updatedBook.getPrice(), "The price should be updated.");
        assertEquals(120, updatedBook.getSoldCopies(), "The sold copies should be updated.");
    }

    @Test
    void deleteBook() {
        //Arrange : this test has passesd last tested 10/27/2024
        Book bookToDelete = new Book(0, "Delete Me", "Author", 10, 10.0, 0);
        bookRepository.addBook(bookToDelete);

        // Find the added book's ID
        List<Book> books = bookRepository.getAllBooks();
        int bookId = books.stream().filter(book -> "Delete Me".equals(book.getTitle()))
                .map(Book::getId).findFirst().orElseThrow();

        // Delete the book
        bookRepository.deleteBook(bookId);

        // Verify deletion
        books = bookRepository.getAllBooks();
        assertFalse(books.stream().anyMatch(book -> book.getId() == bookId),
                "The book should be deleted from the database.");
    }
}
