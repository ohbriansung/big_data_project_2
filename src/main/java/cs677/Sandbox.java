package cs677;

public class Sandbox {
  public static void main(String[] args) {
    itemizeString();
  }

  private static void itemizeString() {
    String location = "abc";
    for (int i = 0; i < 10; i++) {
      String numbered_location = String.format("%s_%04d", location, i );
      System.out.println(numbered_location);
    }
  }
}