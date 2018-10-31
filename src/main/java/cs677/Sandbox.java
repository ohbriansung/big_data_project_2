package cs677;

public class Sandbox {
  public static void main(String[] args) {
    String location = "abc";
    int number = 0;
    String numbered_location = String.format("%s_%04d", location, number);
    System.out.println(numbered_location);
  }
}
