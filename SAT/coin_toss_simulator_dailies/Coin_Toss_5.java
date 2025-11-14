import java.util.*;

class coin {
  private boolean $is_heads;
  private boolean $is_tails;
  private String $face;
  public coin(String _face, boolean _is_heads, boolean _is_tails) {
    $is_heads = _is_heads;
    $is_tails = _is_tails;
    $face = _face;
  }
  public String get_face() {
    return $face;
  }
}

class Coin_Toss_5 {
  public static void main(String[] args) {
    Random $rng = new Random();
    coin $c;
    int $f = $rng.nextInt(2);
    if ($f == 0) {
      $c = new coin(
        "heads", true, false
      );
    }
    else {
      $c = new coin(
        "tails", false, true
      );
    }
    System.out.println($c.get_face());
  }
}
