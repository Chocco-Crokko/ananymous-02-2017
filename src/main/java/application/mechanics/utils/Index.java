package application.mechanics.utils;

@SuppressWarnings({"PublicField", "InstanceVariableNamingConvention"})
public class Index {
    public int i;
    public int j;

    public Index() {
    }

    public Index(int i, int j) {
        this.i = i;
        this.j = j;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Index other = (Index) obj;
        return other.i == i && other.j == j;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(i) + Integer.hashCode(j);
    }
}
