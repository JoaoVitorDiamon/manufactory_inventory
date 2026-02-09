import Dashboard from "./Dashboard";
import { useProducts, useRawMaterials } from "../hooks/useInventoryApi";

export default function DashboardApiConnected() {
  const { data: products = [], isLoading: loadingProducts, error: errorProducts } = useProducts();
  const { data: rawMaterials = [], isLoading: loadingRawMaterials, error: errorRawMaterials } = useRawMaterials();

  if (loadingProducts || loadingRawMaterials) {
    return <div className="p-8 text-center">Loading dashboard...</div>;
  }

  if (errorProducts || errorRawMaterials) {
    return (
      <div className="p-8 text-center text-red-600">
        Error loading data. Please try again later.
      </div>
    );
  }

  return <Dashboard products={products} rawMaterials={rawMaterials} />;
}
