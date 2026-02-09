import { Package, AlertTriangle, TrendingUp } from "lucide-react";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import type { Product, RawMaterial } from "../App";

interface DashboardProps {
  products: Product[];
  rawMaterials: RawMaterial[];
}

export default function Dashboard({ products, rawMaterials }: DashboardProps) {
  const totalProducts = products.length;
  const totalRawMaterials = rawMaterials.length;
  const lowStockCount = rawMaterials.filter((rm) => rm.stockQuantity < 50).length;
  
  const totalInventoryValue = rawMaterials.reduce(
    (sum, rm) => sum + rm.stockQuantity * 10, 
    0
  );

  const stats = [
    {
      key: "products",
      title: "Total Products",
      value: totalProducts,
      description: "Active product catalog",
      icon: Package,
      color: "text-blue-600",
      bgColor: "bg-blue-100",
    },
    {
      key: "rawMaterials",
      title: "Raw Materials",
      value: totalRawMaterials,
      description: "Materials in inventory",
      icon: Package,
      color: "text-green-600",
      bgColor: "bg-green-100",
    },
    {
      key: "lowStock",
      title: "Low Stock",
      value: lowStockCount,
      description: "Materials below threshold",
      icon: AlertTriangle,
      color: "text-orange-600",
      bgColor: "bg-orange-100",
    },
    {
      key: "inventoryValue",
      title: "Inventory Value",
      value: `$${totalInventoryValue.toLocaleString()}`,
      description: "Estimated material value",
      icon: TrendingUp,
      color: "text-purple-600",
      bgColor: "bg-purple-100",
    },
  ];

  return (
    <div className="space-y-8">
      <div>
        <h1>Dashboard</h1>
        <p className="text-muted-foreground mt-2">
          Overview of your manufacturing inventory system
        </p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <Card key={stat.key}>
              <CardHeader className="flex flex-row items-center justify-between pb-2 space-y-0">
                <CardTitle className="text-sm font-medium">{stat.title}</CardTitle>
                <div className={`p-2 rounded-lg ${stat.bgColor}`}>
                  <Icon className={`h-4 w-4 ${stat.color}`} />
                </div>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{stat.value}</div>
                <p className="text-xs text-muted-foreground mt-1">
                  {stat.description}
                </p>
              </CardContent>
            </Card>
          );
        })}
      </div>

      <div className="grid gap-4 md:grid-cols-2">
        <Card>
          <CardHeader>
            <CardTitle>Recent Products</CardTitle>
            <CardDescription>Latest products in your catalog</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {products.slice(0, 5).map((product) => (
                <div
                  key={product.id}
                  className="flex items-center justify-between p-3 rounded-lg border bg-muted/50"
                >
                  <div>
                    <p className="font-medium">{product.name}</p>
                    <p className="text-sm text-muted-foreground">{product.code}</p>
                  </div>
                  <p className="font-semibold">${product.price.toFixed(2)}</p>
                </div>
              ))}
              {products.length === 0 && (
                <p className="text-sm text-muted-foreground text-center py-4">
                  No products available
                </p>
              )}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Stock Status</CardTitle>
            <CardDescription>Raw materials inventory levels</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              {rawMaterials.slice(0, 5).map((material) => (
                <div
                  key={material.id}
                  className="flex items-center justify-between p-3 rounded-lg border bg-muted/50"
                >
                  <div>
                    <p className="font-medium">{material.name}</p>
                    <p className="text-sm text-muted-foreground">{material.code}</p>
                  </div>
                  <div className="text-right">
                    <p className="font-semibold">{material.stockQuantity} units</p>
                    {material.stockQuantity < 50 && (
                      <p className="text-xs text-orange-600">Low Stock</p>
                    )}
                  </div>
                </div>
              ))}
              {rawMaterials.length === 0 && (
                <p className="text-sm text-muted-foreground text-center py-4">
                  No raw materials available
                </p>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
