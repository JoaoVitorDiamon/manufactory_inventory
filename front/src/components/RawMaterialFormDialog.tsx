import { useState, useEffect } from "react";
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
import type { RawMaterial } from "../App";

interface RawMaterialFormDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  material: RawMaterial | null;
  onSave: (material: RawMaterial) => void;
}

export default function RawMaterialFormDialog({
  open,
  onOpenChange,
  material,
  onSave,
}: RawMaterialFormDialogProps) {
  const [formData, setFormData] = useState({
    code: "",
    name: "",
    stockQuantity: "",
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    if (material) {
      setFormData({
        code: material.code,
        name: material.name,
        stockQuantity: material.stockQuantity.toString(),
      });
    } else {
      setFormData({ code: "", name: "", stockQuantity: "" });
    }
    setErrors({});
  }, [material, open]);

  const validate = () => {
    const newErrors: Record<string, string> = {};

    if (!formData.code.trim()) {
      newErrors.code = "Code is required";
    }
    if (!formData.name.trim()) {
      newErrors.name = "Name is required";
    }
    if (
      formData.stockQuantity === "" ||
      parseFloat(formData.stockQuantity) < 0
    ) {
      newErrors.stockQuantity = "Stock quantity must be 0 or greater";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    let stockQuantity = parseInt(formData.stockQuantity, 10);
    if (isNaN(stockQuantity) || formData.stockQuantity === "") {
      stockQuantity = 0;
    }
    const newMaterial: RawMaterial = {
      id: material?.id || Date.now().toString(),
      code: formData.code.trim(),
      name: formData.name.trim(),
      stockQuantity,
    };

    onSave(newMaterial);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <DialogTitle>
            {material ? "Edit Raw Material" : "Create Raw Material"}
          </DialogTitle>
          <DialogDescription>
            {material
              ? "Update raw material details and stock quantity"
              : "Add a new raw material to your inventory"}
          </DialogDescription>
        </DialogHeader>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="code">
              Material Code <span className="text-destructive">*</span>
            </Label>
            <Input
              id="code"
              value={formData.code}
              onChange={(e) =>
                setFormData({ ...formData, code: e.target.value })
              }
              placeholder="e.g., RM001"
              className={errors.code ? "border-destructive" : ""}
            />
            {errors.code && (
              <p className="text-sm text-destructive">{errors.code}</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="name">
              Material Name <span className="text-destructive">*</span>
            </Label>
            <Input
              id="name"
              value={formData.name}
              onChange={(e) =>
                setFormData({ ...formData, name: e.target.value })
              }
              placeholder="e.g., Steel Sheet"
              className={errors.name ? "border-destructive" : ""}
            />
            {errors.name && (
              <p className="text-sm text-destructive">{errors.name}</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="stockQuantity">
              Stock Quantity <span className="text-destructive">*</span>
            </Label>
            <Input
              id="stockQuantity"
              type="number"
              step="0.01"
              min="0"
              value={formData.stockQuantity}
              onChange={(e) =>
                setFormData({ ...formData, stockQuantity: e.target.value })
              }
              placeholder="0"
              className={errors.stockQuantity ? "border-destructive" : ""}
            />
            {errors.stockQuantity && (
              <p className="text-sm text-destructive">{errors.stockQuantity}</p>
            )}
            <p className="text-sm text-muted-foreground">
              Current inventory units available
            </p>
          </div>

          <DialogFooter className="gap-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancel
            </Button>
            <Button type="submit">
              {material ? "Update Material" : "Create Material"}
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
