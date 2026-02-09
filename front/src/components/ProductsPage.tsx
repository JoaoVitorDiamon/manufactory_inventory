import { useState } from "react";
import { Plus, Pencil, Trash2, Search } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "./ui/table";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "./ui/alert-dialog";
import { toast } from "sonner@2.0.3";
import axios from "axios";
import ProductFormDialog from "./ProductFormDialog";
import type { Product, RawMaterial } from "../App";

interface ProductsPageProps {
  products: Product[];
  setProducts: React.Dispatch<React.SetStateAction<Product[]>>;
  rawMaterials: RawMaterial[];
}

export default function ProductsPage({
  products,
  setProducts,
  rawMaterials,
}: ProductsPageProps) {
  const [searchQuery, setSearchQuery] = useState("");
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingProduct, setEditingProduct] = useState<Product | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [productToDelete, setProductToDelete] = useState<Product | null>(null);

  const filteredProducts = (products || []).filter(
    (product) => {
      if (!product || typeof product.name !== "string") return false;
      const code = product.code ?? "";
      return (
        code.toString().toLowerCase().includes(searchQuery.toLowerCase()) ||
        product.name.toLowerCase().includes(searchQuery.toLowerCase())
      );
    }
  );

  const handleCreate = () => {
    setEditingProduct(null);
    setIsFormOpen(true);
  };

  const handleEdit = (product: Product) => {
    setEditingProduct(product);
    setIsFormOpen(true);
  };

  const handleSave = async (product: Product) => {
    try {
      if (editingProduct) {
        const productRequest = {
          code: product.code ?? "",
          name: product.name ?? "",
          price: product.price,
        };
        await axios.put(`http://localhost:8080/api/products/${product.id}`, productRequest);
        toast.success("Produto atualizado com sucesso");
      } else {
        const productRequest = {
          code: product.code,
          name: product.name,
          price: product.price,
        };
        const recipeRequests = (Array.isArray(product.bom) ? product.bom : []).map((item) => ({
          productId: undefined,
          rawMaterialId: item.rawMaterialId,
          quantity: item.requiredQuantity,
        }));
        await axios.post("http://localhost:8080/api/products", {
          productRequest,
          recipeRequests,
        });
        toast.success("Produto criado com sucesso");
      }
      setProducts();
      setIsFormOpen(false);
      setEditingProduct(null);
    } catch (err) {
      toast.error("Erro ao salvar produto");
    }
  };

  const handleDeleteClick = (product: Product) => {
    setProductToDelete(product);
    setDeleteDialogOpen(true);
  };

  const handleDeleteConfirm = async () => {
    if (productToDelete) {
      try {
        await axios.delete(`http://localhost:8080/api/products/${productToDelete.id}`);
        setProducts((prev) => prev.filter((p) => p.id !== productToDelete.id));
        toast.success("Product deleted successfully");
      } catch (err) {
        toast.error("Erro ao deletar produto");
      } finally {
        setProductToDelete(null);
        setDeleteDialogOpen(false);
      }
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1>Products</h1>
          <p className="text-muted-foreground mt-2">
            Manage your product catalog and bill of materials
          </p>
        </div>
        <Button onClick={handleCreate} className="gap-2 sm:w-auto w-full">
          <Plus className="h-4 w-4" />
          Create Product
        </Button>
      </div>
      <div className="flex items-center gap-2">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search by code or name..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9"
          />
        </div>
      </div>
      <div className="border rounded-lg">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Code</TableHead>
                <TableHead>Name</TableHead>
                <TableHead className="text-right">Price</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredProducts.length === 0 ? (
                <TableRow>
                  <TableCell colSpan={5} className="text-center py-8 text-muted-foreground">
                    {searchQuery
                      ? "No products found matching your search"
                      : "No products available. Create one to get started."}
                  </TableCell>
                </TableRow>
              ) : (
                filteredProducts.map((product) => (
                  <TableRow key={product.id}>
                    <TableCell className="font-medium">{product.code ?? ""}</TableCell>
                    <TableCell>{product.name}</TableCell>
                    <TableCell className="text-right">
                      ${product.price.toFixed(2)}
                    </TableCell>
                    <TableCell className="text-right">
                      <div className="flex justify-end gap-2">
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleEdit(product)}
                        >
                          <Pencil className="h-4 w-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="icon"
                          onClick={() => handleDeleteClick(product)}
                        >
                          <Trash2 className="h-4 w-4 text-destructive" />
                        </Button>
                      </div>
                    </TableCell>
                  </TableRow>
                ))
              )}
            </TableBody>
          </Table>
        </div>
      </div>
      <ProductFormDialog
        open={isFormOpen}
        onOpenChange={setIsFormOpen}
        product={editingProduct}
        rawMaterials={rawMaterials}
        onSave={handleSave}
      />
      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Product</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete "{productToDelete?.name}"? This action
              cannot be undone.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Cancel</AlertDialogCancel>
            <AlertDialogAction
              onClick={handleDeleteConfirm}
              className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
            >
              Delete
            </AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
