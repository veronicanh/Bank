// Sorterer listen fra minst foerst til stoerst bakerst
class SortedList<T extends Comparable<T>> extends List<T> {
  // Legger til element paa riktig plass i lista
  @Override
  public void add(T x) {
    if (start == null) {
      super.add(x);
      return;
    }
    Node denne = start;
    int index = 0;
    // Leter gjennom lista til vi finner stoerre data enn det nye
    while (denne != null) {
      if ((denne.data).compareTo(x) > 0) {
        // Legger den nye dataen inn en plass foran
        super.add(index, x);
        return;
      }
      denne = denne.neste;
      index ++;
    }
    super.add(x);
  }
}
