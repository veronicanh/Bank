import java.util.Iterator;

class List<T> implements Iterable<T> {
    // Variabler som tar vare paa start-node og length
    protected Node start = null;
    protected int length = 0;

    // Klasse for noder som lenkes sammen
    class Node {
        Node neste = null;
        T data;

        Node(T x) {
          data = x;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    // Klasse for aa kunne lage Iterator-objekt til Lenkeliste
    class LenkelisteIterator implements Iterator<T> {
        int index = 0;

        @Override
        public boolean hasNext() {
            return (index < length());
        }

        @Override
        public T next() {
            return get(index++);
        }
    }

    // Returerer ett nytt Iterator-objekt
    public Iterator<T> iterator() {
        return new LenkelisteIterator();
    }

    // Hjelpemetode som returnerer noden en plass foran pos
    private Node finnNode(int pos) {
        Node nodeForan = start;
        for (int i = 0; i < pos - 1; i++) {
            nodeForan = nodeForan.neste;
        }
        return nodeForan;
    }

    // Hjelpemetode for aa sjekke om indexen er gyldig
    private void sjekkindex(int pos) throws UgyldigListeindex {
        if (pos < 0 || pos >= length) {
            throw new UgyldigListeindex(pos);
        }
    }

    // Returnerer antall elementer i lista
    public int length() {
        return length;
    }

    // Legger til element paa slutten av lista
    public void add(T x) {
        Node nyNode = new Node(x);
        if (start == null) {
            start = nyNode;
        } else {
            finnNode(length).neste = nyNode;
        }
        length++;
    }

    // Legger inn element paa gitt index
    public void add(int pos, T x) throws UgyldigListeindex {
        // Kan legge til element paa index == length, skal ikke kastes unntak
        if (pos != length) {
            sjekkindex(pos);
        }
        Node nyNode = new Node(x);
        if (pos == 0) {
            nyNode.neste = start;
            start = nyNode;
        } else {
            Node nodeForan = finnNode(pos);
            nyNode.neste = nodeForan.neste;
            nodeForan.neste = nyNode;
        }
        length ++;
    }

    // Returnerer element paa gitt index
    public T get(int pos) throws UgyldigListeindex {
        if (start == null) {
            return null;
        }
        sjekkindex(pos);
        return finnNode(pos + 1).data;
    }

    // Fjerner noden med data som tilsvarer variabelen som sendes med inn
    public void remove(T x) {
        Node denne = start;
        // Finner indeksen til noden som skal fjernes
        int indeks = -1;
        boolean funnet = false;
        while (indeks < length && !funnet) {
            indeks++;
            if (denne.data == x) {
                funnet = true;
            }
            denne = denne.neste;
        }
        // Fjerner noden
        if (indeks == 0) {
            start = start.neste;
        } else {
            Node nodeForan = finnNode(indeks);
            Node slettDenne = nodeForan.neste;
            nodeForan.neste = slettDenne.neste;
        }
        length--;
    }

    // Toemmer lista
    public void clear() {
        start = null;
        length = 0;
    }

    // String-representasjon paa formen "((T x).toString) -> ((T x).toString)"
    @Override
    public String toString() {
        if (start == null) {
            return "tom";
        }
        String str = "";
        Node denne = start;
        while (denne != null) {
            str += denne.toString();
            if (denne.neste != null) {
                str += ", ";
            }
            denne = denne.neste;
        }
        return str;
    }
}

class UgyldigListeindex extends RuntimeException {
    private static final long serialVersionUID = 1L;

    UgyldigListeindex(int index) {
        super("Ugyldig index: " + index);
    }
}
