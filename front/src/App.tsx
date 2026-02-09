import { useState } from "react";
import { useProducts, useRawMaterials } from "./hooks/useInventoryApi";
import { Package, Menu } from "lucide-react";
import { Button } from "./components/ui/button";
import { Sheet, SheetContent, SheetTrigger } from "./components/ui/sheet";
import ProductsPage from "./components/ProductsPage";
import RawMaterialsPage from "./components/RawMaterialsPage";
import ProductionPlanPage from "./components/ProductionPlanPage";
import { Toaster } from "./components/ui/sonner";
import Dashboard from "./components/Dashboard";

export interface Product {
  id: string;
  code: string;
  name: string;
  price: number;
  bom: BOMItem[];
}

export interface RawMaterial {
  id: string;
  code: string;
  name: string;
  stockQuantity: number;
}

export interface BOMItem {
  rawMaterialId: string;
  requiredQuantity: number;
}

export interface ProductionSuggestion {
  productId?: string;
  productCode?: string;
  productName?: string;
  name?: string;
  unitPrice?: number;
  productValue?: number;
  maxQuantity?: number;
  suggestedQuantity?: number;
  totalValue?: number;
}

type Page = "dashboard" | "products" | "rawMaterials" | "productionPlan";

export default function App() {
  const [currentPage, setCurrentPage] = useState<Page>("dashboard");
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const { data: products = [], isLoading: loadingProducts, error: errorProducts, refetch: refetchProducts } = useProducts();
  const { data: rawMaterials = [], isLoading: loadingRawMaterials, error: errorRawMaterials, refetch: refetchRawMaterials } = useRawMaterials();
  const setProducts = () => refetchProducts();
  const setRawMaterials = () => refetchRawMaterials();

  return (
    <div className="min-h-screen bg-background">
      <header className="sticky top-0 z-50 w-full border-b bg-card">
          <div className="flex h-16 items-center gap-4 px-4 lg:px-6">
            <Sheet open={mobileMenuOpen} onOpenChange={setMobileMenuOpen}>
              <SheetTrigger asChild>
                <Button variant="ghost" size="icon" className="lg:hidden">
                  <Menu className="h-6 w-6" />
                </Button>
              </SheetTrigger>
              <SheetContent side="left" className="w-64 p-6">
                <div className="mb-8">
                  <h2 className="flex items-center gap-2">
                    <Package className="h-6 w-6" />
                    MFG Inventory
                  </h2>
                </div>
                <nav className="flex flex-col gap-4">
                  <Button variant={currentPage === "dashboard" ? "default" : "ghost"} onClick={() => { setCurrentPage("dashboard"); setMobileMenuOpen(false); }}>Dashboard</Button>
                  <Button variant={currentPage === "products" ? "default" : "ghost"} onClick={() => { setCurrentPage("products"); setMobileMenuOpen(false); }}>Produtos</Button>
                  <Button variant={currentPage === "rawMaterials" ? "default" : "ghost"} onClick={() => { setCurrentPage("rawMaterials"); setMobileMenuOpen(false); }}>Matérias-primas</Button>
                  <Button variant={currentPage === "productionPlan" ? "default" : "ghost"} onClick={() => { setCurrentPage("productionPlan"); setMobileMenuOpen(false); }}>Plano de Produção</Button>
                </nav>
              </SheetContent>
            </Sheet>
            <div className="flex items-center gap-2">
              <Package className="h-6 w-6" />
              <span className="hidden sm:inline">Manufacturing Inventory System</span>
              <span className="sm:hidden">MFG Inventory</span>
            </div>
          </div>
        </header>
        <div className="flex">
          <aside className="hidden lg:flex w-64 flex-col border-r bg-card min-h-[calc(100vh-4rem)]">
            <div className="p-6">
              <nav className="flex flex-col gap-4">
                <Button variant={currentPage === "dashboard" ? "default" : "ghost"} onClick={() => setCurrentPage("dashboard")}>Dashboard</Button>
                <Button variant={currentPage === "products" ? "default" : "ghost"} onClick={() => setCurrentPage("products")}>Produtos</Button>
                <Button variant={currentPage === "rawMaterials" ? "default" : "ghost"} onClick={() => setCurrentPage("rawMaterials")}>Matérias-primas</Button>
                <Button variant={currentPage === "productionPlan" ? "default" : "ghost"} onClick={() => setCurrentPage("productionPlan")}>Plano de Produção</Button>
              </nav>
            </div>
          </aside>
          <main className="flex-1 p-4 lg:p-8">
            {currentPage === "dashboard" && (
              loadingProducts || loadingRawMaterials ? (
                <div className="p-8 text-center">Carregando dashboard...</div>
              ) : errorProducts || errorRawMaterials ? (
                <div className="p-8 text-center text-red-600">Erro ao carregar dados do backend.</div>
              ) : (
                <Dashboard products={products} rawMaterials={rawMaterials} />
              )
            )}
            {currentPage === "products" && (
              <ProductsPage
                products={products}
                setProducts={setProducts}
                rawMaterials={rawMaterials}
              />
            )}
            {currentPage === "rawMaterials" && (
              <RawMaterialsPage
                rawMaterials={rawMaterials}
                setRawMaterials={setRawMaterials}
              />
            )}
            {currentPage === "productionPlan" && (
              <ProductionPlanPage products={products} rawMaterials={rawMaterials} />
            )}
          </main>
        </div>
        <Toaster />
      </div>
  );
}
