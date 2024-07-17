package book.catalog;

import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.lang.reflect.Type;

import com.google.common.reflect.*;
import com.google.gson.*;
import org.apache.commons.lang3.*;


public class Catalog {
    public List<Book> books;
    
    //Do Not Edit
    public Catalog(String filePath) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();
        Type listType = new TypeToken<List<Book>>(){}.getType();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            List<Book> books = gson.fromJson(bufferedReader, listType);
            this.books = new ArrayList<>(books);
            Catalog.mergeSort(this.books);
        } catch (IOException e) {
            System.err.println("Unable to read file: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Unable to parse JSON: " + e.getMessage());
        }
        
    }

    
    //Quicksort
    public static void swap(List<Book> arr, int i, int j) {
        Book temp = arr.get(i);
        arr.set(i, arr.get(j));
        arr.set(j, temp);
    }

    //Complete the following implementations
    public static int partition(List<Book> arr, int low, int high) {
        Book pivot = arr.get(high);
        int ci = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).compareTo(pivot) < 0) {
                ci++;
                swap(arr,ci,j);
            }
        }
        swap(arr,ci+1,high);
        return ci+1;
    }

    public static void quickSort(List<Book> arr, int low, int high) {

        if (low < high) {
            int pivotIndex = partition(arr, low, high);
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1 , high);
        }
    }
    
    //Merge sort
    public static void merge(List<Book> left, List<Book> right, List<Book> arr) {
        if(left.size() <= 1) return;
        int leftIndex = 0;
        int rightIndex = 0;
        int tempIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if(left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                arr.set(tempIndex, left.get(leftIndex));
                leftIndex++;
            }
            else {
                arr.set(tempIndex, right.get(rightIndex));
                rightIndex++;
            }
            tempIndex++;

        }

        while (leftIndex < left.size()) {
            arr.set(tempIndex,left.get(leftIndex));
            leftIndex++;
            tempIndex++;
        }

        while (rightIndex < right.size()) {
            arr.set(tempIndex,right.get(rightIndex));
            rightIndex++;
            tempIndex++;
        }

    }

    public static void mergeSort(List<Book> arr) {
        int low = 0;
        int high = arr.size();
        int middle =  high / 2;
        if(high <= 1) return;
        List<Book> left = new ArrayList<>(arr.subList(low,middle));
        List<Book> right = new ArrayList<>(arr.subList(middle,high));

        mergeSort(left);
        mergeSort(right);
        merge(left,right, arr);

    }

    //Binary Search
    public static int binarySearch(List<Book> arr, String title) {
        Book query = new Book(title, null, 0, null);
        int low = 0;
        int high = arr.size();
        while(low <= high-1) {
            int mid =  low + (high - low ) / 2;
            Book temp = arr.get(mid);
            if(temp.compareTo(query) > 0){
                high = mid - 1;
            } else if (temp.compareTo(query) < 0) {
                low = mid + 1;
            } else {
                System.out.println("Found");
                return mid;
            }
        }
        return -1;
    }    

    //Do Not Edit
    public void displayBooks(List<Book> arr) {
        for(Book book : arr) {
            System.out.println(book.toString()+"\n");
        }
    }
    
    public void addBook(Scanner scanner) {
        System.out.println("Enter the title of the book:");
        String title = scanner.nextLine();

        if(binarySearch(books, title) != -1){
            System.out.println("A book with the title " + title + " already exists!");
            return;
        }

        if(title.length() == 0) {
            System.out.println("No title was given.");
            return;
        }

        System.out.println("Enter the author of the book (or leave blank if unknown):");
        String author = scanner.nextLine();

        System.out.println("Enter the number of pages in the book (or enter 0 if unknown):");
        String numEntry = scanner.nextLine();
        int numPages = StringUtils.isNumeric(numEntry) ? Integer.parseInt(numEntry) : 0;


        System.out.println("Enter the published date of the book (yyyy-mm-dd) (or leave blank if unknown):");
        String dateEntry = scanner.nextLine();

        LocalDate date = null;
        if(dateEntry.length() > 0) {
            try {
                date = LocalDate.parse(dateEntry);
            }
            catch(DateTimeParseException e) {
                System.out.println("Invalid date given. Date is set to null.");
            }
        }

        System.out.println("Enter the genre(s) of the book separated by commas or spaces (or leave blank if unknown):");
        String[] genres = scanner.nextLine().split("[,\\s]+");

        Book book = new Book(title, author, numPages, date, genres);

        books.add(book);
        quickSort(books, 0, books.size() - 1);

        System.out.println("Book added successfully.");
    }

    public void removeBook(Scanner scanner) {
        System.out.println("Enter the title of the book to remove:");
        String title = scanner.nextLine();

        int index = binarySearch(books, title);

        if(index != -1) {
            books.remove(index);
            System.out.println("Book removed successfully.");
        }
        else{
            System.out.println("Book not found.");
        }
        
    }

    public void editBook(Scanner scanner) {
        System.out.println("Enter the title of the book to edit:");
        String title = scanner.nextLine();

        int index = binarySearch(books, title);
        if(index == -1) {
            System.out.println("Book not found.");
            return;
        }

        Book book = books.get(index);
        System.out.println("Enter the new title of the book (leave blank to keep current title):");
        String newTitle = scanner.nextLine();
        book.setTitle(newTitle.length() > 0 ? newTitle : book.getTitle());

        System.out.println("Enter the new author of the book (leave blank to keep current author):");
        String newAuthor = scanner.nextLine();
        book.setAuthor(newAuthor.length() > 0 ? newAuthor : book.getAuthor());

        System.out.println("Enter the new number of pages in the book (enter 0 or leave blank to keep current number of pages.");
        String numEntry = scanner.nextLine();
        int newPages = StringUtils.isNumeric(numEntry) ? Integer.parseInt(numEntry) : 0;
        book.setNumberOfPages(newPages > 0 ? newPages : book.getNumberOfPages());

        System.out.println("Enter the new genres of book separated by commas or spaces (leave blank to keep current author):");
        String newGenres = scanner.nextLine();
        book.setGenres(newGenres.length() > 0 ? new ArrayList<>(Arrays.asList(newGenres.split("[,\\s]+"))) : book.getGenres());
        
        System.out.println("Enter the new published date of the book (yyyy-mm-dd) (leave blank to keep current published date):");
        String newDate = scanner.nextLine();
        LocalDate date = null;
        if(newDate.length() > 0) {
            try {
                date = LocalDate.parse(newDate);
            }
            catch(DateTimeParseException e) {
                System.out.println("Invalid date given. Date is unchanged.");
            }
        }
        book.setPublishedDate(date != null ? date : book.getPublishedDate());
        mergeSort(books);
    }

    public void searchByTitle(Scanner scanner) {
        System.out.println("Enter the title of the book you are looking for. (leave blank to cancel):");
        String query = scanner.nextLine();

        if(query.length() == 0) {
            return;
        }

        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            }
        }
        displayBooks(results);
    }

    public void searchByAuthor(Scanner scanner) {
        System.out.println("Enter the author of the book(s) you are looking for. (leave blank to cancel):");
        String query = scanner.nextLine();

        if(query.length() == 0) {
            return;
        }

        List<Book> results = new ArrayList<>();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                results.add(book);
            }
        }
        displayBooks(results);
    }

    public void searchByGenre(Scanner scanner) {
        System.out.println("Enter the genre(s) of the book(s) you are looking for separated by commas or spaces. (leave blank to cancel):");
        String query = scanner.nextLine();

        if(query.length() == 0) {
            return;
        }
        List<Book> results = new ArrayList<>();
        List<String> queryList = new ArrayList<>(List.of(query.split("[,\\s]+")));

        for (Book book : books) {
            List<String> genres = book.getGenres();
            if (genres.containsAll(queryList)) {
                results.add(book);
            }
        }
        displayBooks(results);
    }

}
