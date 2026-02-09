import { useState } from "react";
import { useQuery } from "@tanstack/react-query";
import axios from "axios";
import { Sparkles, AlertCircle, TrendingUp, Package } from "lucide-react";
import { Button } from "./ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
  TableFooter,
} from "./ui/table";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "./ui/card";
import { Alert, AlertDescription, AlertTitle } from "./ui/alert";

import type { Product, RawMaterial, ProductionSuggestion } from "../App";

interface ProductionPlanPageProps {
  products: Product[];
  rawMaterials: RawMaterial[];
}

export default function ProductionPlanPage({ products, rawMaterials }: ProductionPlanPageProps) {
  const [hasGenerated, setHasGenerated] = useState(false);
  const [productionPlan, setProductionPlan] = useState<ProductionSuggestion[]>([]);

  const { isLoading, refetch } = useQuery({
    queryKey: ["production-suggestions"],
    queryFn: async () => {
      const { data } = await axios.get("http://localhost:8080/product-recipes/production-suggestions");
      return data;
    },
    enabled: false,
  });

  const generateProductionPlan = async () => {
    const result = await refetch();
    if (result.data) {
      setProductionPlan(result.data);
    } else {
      setProductionPlan([]);
    }
    setHasGenerated(true);
  };

  const totalExpectedValue = productionPlan.reduce((sum, item) => {
    const value = typeof item.totalValue === "number"
      ? item.totalValue
      : typeof item.productValue === "number" && typeof item.maxQuantity === "number"
        ? item.productValue * item.maxQuantity
        : 0;
    return sum + value;
  }, 0);

  const totalUnits = productionPlan.reduce((sum, item) => {
    const qty = typeof item.suggestedQuantity === "number"
      ? item.suggestedQuantity
      : typeof item.maxQuantity === "number"
        ? item.maxQuantity
        : 0;
    return sum + qty;
  }, 0);

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div>
          <h1>Production Plan</h1>
          <p className="text-muted-foreground mt-2">
            Generate optimal production suggestions based on available inventory
          </p>
        </div>
        <div className="flex gap-2">
          <Button onClick={generateProductionPlan} className="gap-2 sm:w-auto w-full">
            <Sparkles className="h-4 w-4" />
            Generate Plan
          </Button>
          {productionPlan.length > 0 && (
            <Button
              variant="secondary"
              className="gap-2 sm:w-auto w-full"
              onClick={async () => {
                try {
                  await axios.post("http://localhost:8080/execute-production", productionPlan);
                  alert("Produção executada com sucesso! Estoques atualizados.");
                } catch (err) {
                  alert("Erro ao executar produção. Verifique o backend.");
                }
              }}
            >
              Produzir
            </Button>
          )}
        </div>
      </div>
      <Alert>
        <AlertCircle className="h-4 w-4" />
        <AlertTitle>Production Prioritization</AlertTitle>
        <AlertDescription>
          The system suggests production starting from the highest unit price products
          first. This maximizes revenue when raw materials are shared among multiple
          products.
        </AlertDescription>
      </Alert>
      {isLoading ? (
        <div className="p-8 text-center">Carregando sugestões de produção...</div>
      ) : !hasGenerated ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <Package className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="mb-2">No Production Plan Generated</h3>
            <p className="text-sm text-muted-foreground text-center max-w-md">
              Click "Generate Plan" to create production suggestions based on your
              current raw materials inventory and product catalog.
            </p>
          </CardContent>
        </Card>
      ) : productionPlan.length === 0 ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <AlertCircle className="h-12 w-12 text-muted-foreground mb-4" />
            <h3 className="mb-2">No Production Possible</h3>
            <p className="text-sm text-muted-foreground text-center max-w-md">
              Insufficient raw materials to produce any products. Please restock your
              inventory or adjust product BOMs.
            </p>
          </CardContent>
        </Card>
      ) : (
        <>
          <div className="grid gap-4 sm:grid-cols-3">
            <Card>
              <CardHeader className="pb-3">
                <CardDescription>Total Expected Value</CardDescription>
                <CardTitle className="text-3xl">
                  ${totalExpectedValue.toLocaleString(undefined, {
                    minimumFractionDigits: 2,
                    maximumFractionDigits: 2,
                  })}
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex items-center gap-2 text-sm text-muted-foreground">
                  <TrendingUp className="h-4 w-4 text-green-600" />
                  Revenue from suggested production
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="pb-3">
                <CardDescription>Total Units</CardDescription>
                <CardTitle className="text-3xl">{totalUnits}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">
                  Products to be manufactured
                </p>
              </CardContent>
            </Card>

            <Card>
              <CardHeader className="pb-3">
                <CardDescription>Product Types</CardDescription>
                <CardTitle className="text-3xl">{productionPlan.length}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-sm text-muted-foreground">
                  Different products in plan
                </p>
              </CardContent>
            </Card>
          </div>
          <Card>
            <CardHeader>
              <CardTitle>Suggested Production Schedule</CardTitle>
              <CardDescription>
                Prioritized by unit price to maximize revenue
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="border rounded-lg overflow-hidden">
                <div className="overflow-x-auto">
                  <Table>
                    <TableHeader>
                      <TableRow>
                        <TableHead>Product Code</TableHead>
                        <TableHead>Product Name</TableHead>
                        <TableHead className="text-right">Unit Price</TableHead>
                        <TableHead className="text-right">Suggested Qty</TableHead>
                        <TableHead className="text-right">Total Value</TableHead>
                      </TableRow>
                    </TableHeader>
                    <TableBody>
                      {productionPlan.map((item, idx) => {
                        const code = item.productCode || item.productId || idx + 1;
                        const name = item.productName || item.name;
                        const unitPrice = item.unitPrice ?? item.productValue;
                        const suggestedQuantity = item.suggestedQuantity ?? item.maxQuantity;
                        const totalValue = item.totalValue;
                        return (
                          <TableRow key={code}>
                            <TableCell className="font-medium">{code}</TableCell>
                            <TableCell>{name}</TableCell>
                            <TableCell className="text-right">
                              {typeof unitPrice === "number"
                                ? `$${unitPrice.toFixed(2)}`
                                : "-"}
                            </TableCell>
                            <TableCell className="text-right">
                              {typeof suggestedQuantity === "number" ? `${suggestedQuantity} units` : "-"}
                            </TableCell>
                            <TableCell className="text-right font-semibold">
                              {typeof totalValue === "number"
                                ? `$${totalValue.toLocaleString(undefined, {
                                    minimumFractionDigits: 2,
                                    maximumFractionDigits: 2,
                                  })}`
                                : "-"}
                            </TableCell>
                          </TableRow>
                        );
                      })}
                    </TableBody>
                    <TableFooter>
                      <TableRow>
                        <TableCell colSpan={3}>Total</TableCell>
                        <TableCell className="text-right font-semibold">
                          {typeof totalUnits === "number" && !isNaN(totalUnits) ? `${totalUnits} units` : "-"}
                        </TableCell>
                        <TableCell className="text-right font-semibold">
                          ${totalExpectedValue.toLocaleString(undefined, {
                            minimumFractionDigits: 2,
                            maximumFractionDigits: 2,
                          })}
                        </TableCell>
                      </TableRow>
                    </TableFooter>
                  </Table>
                </div>
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  );
}
