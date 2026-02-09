import { useState, useEffect } from "react";
import { Plus, Trash2 } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "./ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "./ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "./ui/table";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import type { Product, RawMaterial, BOMItem } from "../App";

interface ProductFormDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  product: Product | null;
  rawMaterials: RawMaterial[];
  onSave: (product: Product) => void;
}

export default function ProductFormDialog({
  open,
  onOpenChange,
  product,
  rawMaterials,
  onSave,
}: ProductFormDialogProps) {
  const [formData, setFormData] = useState({
    code: "",
    name: "",
    price: "",
  });
  const [bom, setBom] = useState<BOMItem[]>([]);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [selectedRawMaterialId, setSelectedRawMaterialId] = useState<string>("");
  const [requiredQuantity, setRequiredQuantity] = useState<string>("");

  useEffect(() => {
    if (product) {
      setFormData({
        code: product.code || "",
        name: product.name || "",
        price: product.price != null ? product.price.toString() : "",
      });
      setBom(Array.isArray(product.bom) ? product.bom : []);
    } else {
      setFormData({ code: "", name: "", price: "" });
      setBom([]);
    }
    setErrors({});
    setSelectedRawMaterialId("");
    setRequiredQuantity("");
  }, [product, open]);

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (!formData.code.trim()) {
      newErrors.code = "Code is required";
    }
    if (!formData.name.trim()) {
      newErrors.name = "Name is required";
    }
    if (!formData.price || parseFloat(formData.price) < 0) {
      newErrors.price = "Price must be 0 or greater";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  const safeBOM = Array.isArray(bom) ? bom : [];

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;
    const newProduct: Product = {
      id: product?.id || Date.now().toString(),
      code: formData.code.trim(),
      name: formData.name.trim(),
      price: parseFloat(formData.price),
      bom: safeBOM,
    };
    onSave(newProduct);
  };

  const handleAddBOMItem = () => {
    if (!selectedRawMaterialId) return;
    const qty = parseFloat(requiredQuantity);
    if (!qty || qty <= 0) return;
    if (safeBOM.some((item) => item.rawMaterialId === selectedRawMaterialId)) return;
    setBom([...safeBOM, { rawMaterialId: selectedRawMaterialId, requiredQuantity: qty }]);
    setSelectedRawMaterialId("");
    setRequiredQuantity("");
  };

  const handleRemoveBOMItem = (rawMaterialId: string) => {
    setBom(safeBOM.filter((item) => item.rawMaterialId !== rawMaterialId));
  };

  const getRawMaterialName = (id: string) => {
    return rawMaterials.find((rm) => rm.id === id)?.name || "Unknown";
  };

  const getRawMaterialCode = (id: string) => {
    return rawMaterials.find((rm) => rm.id === id)?.code || "";
  };

  const availableRawMaterials = rawMaterials.filter(
    (rm) => !safeBOM.some((item) => item.rawMaterialId === rm.id)
  );

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>{product ? "Edit Product" : "Create Product"}</DialogTitle>
          <DialogDescription>
            {product
              ? "Update product details and bill of materials"
              : "Add a new product to your catalog"}
          </DialogDescription>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-6">
          {/* Basic Information */}
          <div className="space-y-4">
            <div className="grid gap-4 sm:grid-cols-2">
              <div className="space-y-2">
                <Label htmlFor="code">
                  Product Code <span className="text-destructive">*</span>
                </Label>
                <Input
                  id="code"
                  value={formData.code}
                  onChange={(e) =>
                    setFormData({ ...formData, code: e.target.value })
                  }
                  placeholder="e.g., P001"
                  className={errors.code ? "border-destructive" : ""}
                />
                {errors.code && (
                  <p className="text-sm text-destructive">{errors.code}</p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="price">
                  Price ($) <span className="text-destructive">*</span>
                </Label>
                <Input
                  id="price"
                  type="number"
                  step="0.01"
                  min="0"
                  value={formData.price}
                  onChange={(e) =>
                    setFormData({ ...formData, price: e.target.value })
                  }
                  placeholder="0.00"
                  className={errors.price ? "border-destructive" : ""}
                />
                {errors.price && (
                  <p className="text-sm text-destructive">{errors.price}</p>
                )}
              </div>
            </div>

            <div className="space-y-2">
              <Label htmlFor="name">
                Product Name <span className="text-destructive">*</span>
              </Label>
              <Input
                id="name"
                value={formData.name}
                onChange={(e) =>
                  setFormData({ ...formData, name: e.target.value })
                }
                placeholder="e.g., Premium Widget"
                className={errors.name ? "border-destructive" : ""}
              />
              {errors.name && (
                <p className="text-sm text-destructive">{errors.name}</p>
              )}
            </div>

          </div>

          <Card>
            <CardHeader>
              <CardTitle>Bill of Materials (BOM)</CardTitle>
              <CardDescription>
                Define the raw materials required to produce one unit of this product
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="flex flex-col sm:flex-row gap-2">
                <div className="flex-1">
                  <Select
                    value={selectedRawMaterialId}
                    onValueChange={setSelectedRawMaterialId}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select raw material" />
                    </SelectTrigger>
                    <SelectContent>
                      {availableRawMaterials.length === 0 ? (
                        <div className="p-2 text-sm text-muted-foreground text-center">
                          No available materials
                        </div>
                      ) : (
                        availableRawMaterials.map((rm) => (
                          <SelectItem key={rm.id} value={rm.id}>
                            {rm.code} - {rm.name}
                          </SelectItem>
                        ))
                      )}
                    </SelectContent>
                  </Select>
                </div>
                <div className="w-full sm:w-32">
                  <Input
                    type="number"
                    step="0.01"
                    min="0.01"
                    placeholder="Quantity"
                    value={requiredQuantity}
                    onChange={(e) => setRequiredQuantity(e.target.value)}
                  />
                </div>
                <Button
                  type="button"
                  onClick={handleAddBOMItem}
                  disabled={!selectedRawMaterialId || !requiredQuantity}
                  className="gap-2 w-full sm:w-auto"
                >
                  <Plus className="h-4 w-4" />
                  Add
                </Button>
              </div>
              {safeBOM.length > 0 ? (
                <div className="border rounded-lg">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Code</TableHead>
                        <TableHead>Material</TableHead>
                        <TableHead className="text-right">Required Qty</TableHead>
                        <TableHead className="w-16"></TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {safeBOM.map((item) => (
                        <TableRow key={item.rawMaterialId}>
                          <TableCell className="font-medium">
                            {getRawMaterialCode(item.rawMaterialId)}
                          </TableCell>
                          <TableCell>
                            {getRawMaterialName(item.rawMaterialId)}
                          </TableCell>
                          <TableCell className="text-right">
                            {item.requiredQuantity}
                          </TableCell>
                          <TableCell>
                            <Button
                              type="button"
                              variant="ghost"
                              size="icon"
                              onClick={() =>
                                handleRemoveBOMItem(item.rawMaterialId)
                              }
                            >
                              <Trash2 className="h-4 w-4 text-destructive" />
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))}
                    </TableBody>
                  </Table>
                </div>
              ) : (
                <div className="text-center py-6 text-sm text-muted-foreground border rounded-lg bg-muted/50">
                  No materials added to BOM yet
                </div>
              )}
            </CardContent>
          </Card>

          <DialogFooter className="gap-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancel
            </Button>
            <Button type="submit">
              {product ? "Update Product" : "Create Product"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
