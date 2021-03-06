//Author : Shin

import java.lang.*;
import java.util.*;

class IndexMinPQ<Key extends Comparable<Key>>
{
    private int maxN;
    private int n;
    private int pq[];
    private int qp[];
    private Key keys[];

    IndexMinPQ(int maxN)
    {
        if (maxN < 0) throw new IllegalArgumentException();
        this.maxN = maxN;
        this.n    = 0;
        keys = (Key[])new Comparable[maxN];
        pq = new int[maxN];
        qp = new int[maxN];

        for (int i = 0; i < maxN; ++i)
            qp[i] = -1;
    }

    public boolean is_empty()  { return n == 0; }

    public boolean contains(int i)
    {
        if (i < 0 || i > maxN) throw new IndexOutOfBoundsException();
        return qp[i] != -1;
    }

    public int size()  { return n; }

    public void insert(int k, Key key)
    {
        if (k < 0 || k > maxN)  throw new IndexOutOfBoundsException();
        if (contains(k))  throw new IllegalArgumentException();
        qp[k] = n;
        pq[n] = k;
        keys[k] = key;
        swim(n);
        n++;
    }

    public int min_index()  { return pq[0]; }

    public Key min_key()  { return keys[pq[0]]; }

    public int del_min()
    {
        int min = pq[0];
        swap(0, --n);
        sink(0);
        qp[min] = -1;
        keys[min] = null;
        pq[n] = -1;
        return min;
    }

    public Key key_of(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        else return keys[i];
    }

    public void change_key(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        swim(qp[i]);
        sink(qp[i]);
    }

    public void decrease_key(int i, Key key)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        System.out.println(keys[i] + " " + key);
        if (keys[i].compareTo(key) <= 0)  throw new IllegalArgumentException();
        keys[i] = key;
        swim(qp[i]);
    }

    public void increase_key(int i, Key key)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i))       throw new NoSuchElementException();
        if (keys[i].compareTo(key) >= 0)  throw new IllegalArgumentException();
        keys[i] = key;
        sink(qp[i]);
    }

    public void delete(int i)
    {
        if (i < 0 || i > maxN)  throw new IndexOutOfBoundsException();
        if (!contains(i)) throw new NoSuchElementException();
        int index = qp[i];
        swap(index, --n);
        swim(index);
        sink(index);
        keys[i] = null;
        qp[i] = -1;
    }

    private boolean greater(int i, int j)
    {
        return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
    }

    void swap(int i, int j)
    {
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    private void swim(int k)
    {
        while (k > 0 && greater((k - 1) / 2, k))
        {
            swap(k, (k - 1) / 2);
            k = (k - 1) / 2;
        }
    }

    private void sink(int k)
    {
        while (2 * k + 2 < n)
        {
            int j = k * 2 + 1;
            if (j < n &&  greater(j, j+1)) ++j;
            if (!greater(k, j))  break;
            swap(k, j);
            k = j;
        }
    }

    public Iterable<Integer> index()
    {
        IndexMinPQ<Key> copy = new IndexMinPQ<Key>(pq.length);
        Queue<Integer> list  = new LinkedList<Integer>();

        for (int i = 0; i < n; ++i)
            copy.insert(pq[i], keys[pq[i]]);

        while (!copy.is_empty())  list.add(copy.del_min());

        return list;
    }
}


class Bags<Item> implements Iterable<Item>
{
    private Node<Item> first;
    private int n;

    private static class Node<Item>
    {
        private Item item;
        private Node<Item> next;
    }

    public Bags()
    {
        first = null;
        n = 0;
    }

    public boolean is_empty()
    {
        return first == null;
    }

    public int size()
    {
        return n;
    }

    public void add(Item item)
    {
        Node<Item> temp = new Node<Item>();
        temp.item = item;
        temp.next = first;
        first = temp;
    }

    public Iterator<Item> iterator()
    {
        return new ListIterator<Item>(first);
    }

    private class ListIterator<Item> implements Iterator<Item>
    {
        private Node<Item> current;

        public ListIterator(Node<Item> first)
        {
            current = first;
        }

        public boolean hasNext()  { return current != null; }
        public void remove()  { throw new UnsupportedOperationException(); }

        public Item next()
        {
            if (!hasNext())  throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
}

class WeightedEdge implements Comparable<WeightedEdge>
{
    private final int v;
    private final int w;
    private final double weight;

    WeightedEdge(int v, int w, double weight)
    {
        if (v < 0 || w < 0)  throw new IllegalArgumentException();
        if (Double.isNaN(weight))  throw new IllegalArgumentException();

        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    public int from()  { return v; }

    public int to()    { return w; }

    public double weight()  { return weight; }

    public int compareTo(WeightedEdge that)  { return Double.compare(this.weight, that.weight); }

    public String toString()  { return new String(v + "->" + w + " " + weight); }
}

class WeightedDigraph
{
    private final int V;
    private int E;
    Bags<WeightedEdge> adj[];
    private int inDegree[];

    WeightedDigraph(int V)
    {
        this.V = V;
        this.E = 0;
        inDegree = new int[V];
        adj = (Bags<WeightedEdge>[]) new Bags[V];

        for (int i = 0; i < V; ++i)
            adj[i] = new Bags<WeightedEdge>();
    }

    WeightedDigraph(WeightedDigraph G)
    {
        this(G.V());
        this.E = G.E();
        for (int v = 0; v < G.V(); ++v)
            inDegree[v] = G.in_degree(v);

        for (int v = 0; v < G.V(); ++v)
        {
            LinkedList<WeightedEdge> reverse = new LinkedList<WeightedEdge>();
            for (WeightedEdge e : G.adj(v))  reverse.addFirst(e);
            for (WeightedEdge e : reverse )  adj[v].add(e);
        }
    }

    private void valid_vertex(int v)
    {
        if (v < 0 || v >= V)  throw new IndexOutOfBoundsException();
    }

    public Iterable<WeightedEdge> adj(int v)
    {
        valid_vertex(v);
        return adj[v];
    }

    public int V()  { return V; }
    public int E()  { return E; }

    public void add_edge(WeightedEdge e)
    {
        int v = e.from();
        int w = e.to();

        valid_vertex(v);
        valid_vertex(w);

        adj[v].add(e);
        E++;
        inDegree[w]++;
    }

    public int in_degree(int v)
    {
        valid_vertex(v);
        return inDegree[v];
    }

    public int out_degree(int v)
    {
        valid_vertex(v);
        return adj[v].size();
    }

    public Iterable<WeightedEdge> edges()
    {
        Queue<WeightedEdge> list = new LinkedList<WeightedEdge>();
        for (int v = 0; v < V(); ++v)
            for (WeightedEdge e : adj(v))  list.add(e);
        return list;
    }

    public String toString()
    {
        StringBuilder str = new StringBuilder();
        str.append("V = " + V + " E = " + E + "\n");
        for (int v = 0; v < V(); ++v)
        {
            str.append(v + " : ");
            for (WeightedEdge x : adj(v))
                str.append(x + " ");
            str.append("\n");
        }
        return str.toString();
    }
}

class DijkstraSP {
    private double distTo[];
    private WeightedEdge edgeTo[];
    private IndexMinPQ<Double> pq;

    DijkstraSP(WeightedDigraph G, int s)
    {
        for (WeightedEdge e : G.edges())
            if (e.weight() < 0)  throw new IllegalArgumentException();

        distTo = new double[G.V()];
        edgeTo = new WeightedEdge[G.V()];
        pq = new IndexMinPQ<Double>(G.V());

        for (int i = 0; i < G.V(); ++i)
            distTo[i] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        pq.insert(s, distTo[s]);
        while (!pq.is_empty())
        {
            int v = pq.del_min();
            for (WeightedEdge e : G.adj(v))
                relax(e);
        }
    }

    private void relax(WeightedEdge e)
    {
        int v = e.from();
        int w = e.to();
        if (distTo[w] > distTo[v] + e.weight())
        {
            edgeTo[w] = e;
            distTo[w] = distTo[v] + e.weight();
            if (pq.contains(w)) pq.decrease_key(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }
    }

    public double dist_to(int v) { return distTo[v]; }

    public boolean has_path_to(int v)  { return distTo[v] < Double.POSITIVE_INFINITY; }

    public Iterable<WeightedEdge> path(int v)
    {
        if (!has_path_to(v))  return null;

        LinkedList<WeightedEdge> path = new LinkedList<WeightedEdge>();
        for (WeightedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.addFirst(e);
        return path;
    }
}

class RunApp
{
    public static void main(String arg[])
    {
        //Take input
        Scanner readIn = new Scanner(System.in);
        int V = readIn.nextInt();
        int E = readIn.nextInt();
        WeightedDigraph G = new WeightedDigraph(V);
        WeightedEdge e;
        int v,w;
        double wt;
        for (int i = 0; i < E; ++i)
        {
            v  = readIn.nextInt();
            w  = readIn.nextInt();
            wt = readIn.nextDouble();
            e = new WeightedEdge(v,w,wt);
            G.add_edge(e);
        }

        //Calculate Shortest path from vertex : s
        int s = readIn.nextInt();
        DijkstraSP sp = new DijkstraSP(G, s);

        //Print the paths
        for (int t = 0; t < G.V(); ++t)
        {
            if (sp.has_path_to(t))
            {
                System.out.println("\n" + s + " to " + t + " : " + sp.dist_to(t));
                for (WeightedEdge x : sp.path(t))
                    System.out.println(x);
            }
        }

    }
}