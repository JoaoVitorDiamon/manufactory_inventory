import { useQuery } from "@tanstack/react-query";
import axios from "axios";

export function useProducts() {
  return useQuery({
    queryKey: ["products"],
    queryFn: async () => {
      const { data } = await axios.get("http://localhost:8080/api/products");
      return data;
    },
  });
}

export function useRawMaterials() {
  return useQuery({
    queryKey: ["rawMaterials"],
    queryFn: async () => {
      const { data } = await axios.get("http://localhost:8080/api/raw-materials");
      return data;
    },
  });
}
