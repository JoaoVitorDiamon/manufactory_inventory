import { useState } from "react";
import { Plus, Pencil, Trash2, Search, AlertTriangle } from "lucide-react";
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
import { Badge } from "./ui/badge";
import { toast } from "sonner@2.0.3";
import axios from "axios";
import RawMaterialFormDialog from "./RawMaterialFormDialog";
import type { RawMaterial } from "../App";

interface RawMaterialsPageProps {
  rawMaterials: RawMaterial[];
  setRawMaterials: React.Dispatch<React.SetStateAction<RawMaterial[]>>;
}

export default function RawMaterialsPage({
  rawMaterials,
  setRawMaterials,
}: RawMaterialsPageProps) {
  const [searchQuery, setSearchQuery] = useState("");
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingMaterial, setEditingMaterial] = useState<RawMaterial | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [materialToDelete, setMaterialToDelete] = useState<RawMaterial | null>(
    null
  );

  const filteredMaterials = rawMaterials.filter(
    (material) =>
      material.code.toLowerCase().includes(searchQuery.toLowerCase()) ||
      material.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleCreate = () => {
    setEditingMaterial(null);
    setIsFormOpen(true);
  };

  const handleEdit = (material: RawMaterial) => {
    setEditingMaterial(material);
    setIsFormOpen(true);
  };

  const handleSave = async (material: RawMaterial) => {
    if (editingMaterial) {
      setRawMaterials((prev) =>
        prev.map((m) => (m.id === material.id ? material : m))
      );
      toast.success("Raw material updated successfully");
    } else {
      try {
        const req = {
          code: material.code,
          name: material.name,
          stockQuantity: material.stockQuantity,
        };
        const { data } = await axios.post("http://localhost:8080/api/raw-materials", req);
        setRawMaterials((prev) => [...prev, data]);
        toast.success("Raw material created successfully");
      } catch (err) {
        toast.error("Erro ao criar matéria-prima");
      }
    }
    setIsFormOpen(false);
    setEditingMaterial(null);
  };

  const handleDeleteClick = (material: RawMaterial) => {
    setMaterialToDelete(material);
    setDeleteDialogOpen(true);
  };

  const handleDeleteConfirm = async () => {
    if (materialToDelete) {
      try {
        await axios.delete(`http://localhost:8080/api/raw-materials/${materialToDelete.id}`);
        setRawMaterials((prev) => prev.filter((m) => m.id !== materialToDelete.id));
        toast.success("Raw material deleted successfully");
      } catch (err) {
        toast.error("Erro ao deletar matéria-prima");
      } finally {
        setMaterialToDelete(null);
        setDeleteDialogOpen(false);
      }
    }
  };

  const getStockStatus = (quantity: number) => {
    if (quantity === 0) {
      return { label: "Out of Stock", variant: "destructive" as const };
    } else if (quantity < 50) {
      return { label: "Low Stock", variant: "secondary" as const };
    }
    return { label: "In Stock", variant: "default" as const };
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1>Raw Materials</h1>
          <p className="text-muted-foreground mt-2">
            Manage your raw materials inventory and stock levels
          </p>
        </div>
        <Button onClick={handleCreate} className="gap-2 sm:w-auto w-full">
          <Plus className="h-4 w-4" />
          Create Raw Material
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
      <div className="grid gap-4 sm:grid-cols-3">
        <div className="p-4 border rounded-lg bg-card">
          <p className="text-sm text-muted-foreground">Total Materials</p>
          <p className="text-2xl font-bold mt-1">{rawMaterials.length}</p>
        </div>
        <div className="p-4 border rounded-lg bg-card">
          <p className="text-sm text-muted-foreground">Low Stock Items</p>
          <p className="text-2xl font-bold mt-1 text-orange-600">
            {rawMaterials.filter((m) => m.stockQuantity < 50 && m.stockQuantity > 0).length}
          </p>
        </div>
        <div className="p-4 border rounded-lg bg-card">
          <p className="text-sm text-muted-foreground">Out of Stock</p>
          <p className="text-2xl font-bold mt-1 text-destructive">
            {rawMaterials.filter((m) => m.stockQuantity === 0).length}
          </p>
        </div>
      </div>
      <div className="border rounded-lg">
        <div className="overflow-x-auto">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Code</TableHead>
                <TableHead>Name</TableHead>
                <TableHead className="text-right">Stock Quantity</TableHead>
                <TableHead>Status</TableHead>
                <TableHead className="text-right">Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredMaterials.length === 0 ? (
                <TableRow>
                  <TableCell
                    colSpan={5}
                    className="text-center py-8 text-muted-foreground"
                  >
                    {searchQuery
                      ? "No raw materials found matching your search"
                      : "No raw materials available. Create one to get started."}
                  </TableCell>
                </TableRow>
              ) : (
                filteredMaterials.map((material) => {
                  const status = getStockStatus(material.stockQuantity);
                  return (
                    <TableRow key={material.id}>
                      <TableCell className="font-medium">
                        {material.code}
                      </TableCell>
                      <TableCell>{material.name}</TableCell>
                      <TableCell className="text-right">
                        <div className="flex items-center justify-end gap-2">
                          {material.stockQuantity < 50 &&
                            material.stockQuantity > 0 && (
                              <AlertTriangle className="h-4 w-4 text-orange-600" />
                            )}
                          {material.stockQuantity === 0 && (
                            <AlertTriangle className="h-4 w-4 text-destructive" />
                          )}
                          <span>{material.stockQuantity} units</span>
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant={status.variant}>{status.label}</Badge>
                      </TableCell>
                      <TableCell className="text-right">
                        <div className="flex justify-end gap-2">
                          <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => handleEdit(material)}
                          >
                            <Pencil className="h-4 w-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => handleDeleteClick(material)}
                          >
                            <Trash2 className="h-4 w-4 text-destructive" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  );
                })
              )}
            </TableBody>
          </Table>
        </div>
      </div>
      <RawMaterialFormDialog
        open={isFormOpen}
        onOpenChange={setIsFormOpen}
        material={editingMaterial}
        onSave={handleSave}
      />
      <AlertDialog open={deleteDialogOpen} onOpenChange={setDeleteDialogOpen}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Delete Raw Material</AlertDialogTitle>
            <AlertDialogDescription>
              Are you sure you want to delete "{materialToDelete?.name}"? This
              action cannot be undone and may affect product BOMs.
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
