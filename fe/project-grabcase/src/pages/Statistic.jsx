import React, { useState, useEffect } from "react";
import "../App.css";
import axios from "axios";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "../components/ui/Card";

import { Tabs, TabsContent } from "../components/ui/Tabs";
import PieChart from "../components/demo/PieCharts";

const formatRupiah = (number) => {
  return new Intl.NumberFormat("id-ID", {
    style: "currency",
    currency: "IDR",
    minimumFractionDigits: 0,
  }).format(number);
};

export default function Statistic() {
  const [isMobile, setIsMobile] = useState(false);
  const [statisticsData, setStatisticsData] = useState(null);
  const [selectedState, setSelectedState] = useState("");

  useEffect(() => {
    const fetchStatisticsData = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/orders/statistics");
        setStatisticsData(response.data.data);
      } catch (error) {
        console.error("Error fetching statistics data:", error);
      }
    };

    fetchStatisticsData();
  }, []);

  useEffect(() => {
    function handleResize() {
      setIsMobile(window.innerWidth <= 768);
    }

    window.addEventListener("resize", handleResize);
    handleResize();
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  const generatePieChartData = () => {
    if (!statisticsData) return null;

    const { averageOrderPrice, total_order_price } = statisticsData;

    const data = {
      datasets: [
        {
          data: [averageOrderPrice, total_order_price],
          backgroundColor: ["rgba(75, 192, 192, 0.6)", "rgba(128, 128, 128, 1)"],
        },
      ],
      labels: ["Harga rata-rata pesanan", "Total Harga pesanan"],
    };

    return data;
  };

  const chartData = generatePieChartData();

  return (
    <>
      <h1 className="font-bold text-4xl text-center">Statistik</h1>
      <div className="pt-10 pb-20">
        <Tabs defaultValue="orders" className="w-[ 1400px]">
          <TabsContent value="orders">
            <div className="flex justify-center items-center pb-14">
              <div className="max-w-[590px]">{chartData && <PieChart data={chartData} />}</div>
            </div>
            <div className="flex flex-nowrap gap-5">
              <div className="w-full sm:w-1/2 md:w-1/2 lg:w-1/2 xl:w-1/2 p-4">
                <Card className="bg-green-50">
                  {/* Card content */}
                  <CardHeader>
                    <CardTitle>Total makanan</CardTitle>
                    <CardDescription>Jumlah item makanan terjual</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <h3 className=" text-2xl font-semibold tracking-tight">{statisticsData ? statisticsData.total_item : ""}</h3>
                  </CardContent>
                </Card>
              </div>
              <div className="w-full sm:w-1/2 md:w-1/2 lg:w-1/2 xl:w-1/2 p-4">
                <Card className="bg-green-50">
                  {/* Card content */}
                  <CardHeader>
                    <CardTitle>Total Pesanan</CardTitle>
                    <CardDescription>Jumlah order makanan terjual</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <h3 className=" text-2xl font-semibold tracking-tight">{statisticsData ? statisticsData.total_order : ""}</h3>
                  </CardContent>
                </Card>
              </div>
            </div>
            <div className="flex flex-nowrap gap-5">
              <div className="w-full sm:w-1/2 md:w-1/2 lg:w-1/2 xl:w-1/2 p-4">
                <Card className="bg-green-50">
                  {/* Card content */}
                  <CardHeader>
                    <CardTitle>Total Harga Pesanan </CardTitle>
                    <CardDescription>jumlah total harga pesanan</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <h3 className=" text-2xl font-semibold tracking-tight">{statisticsData ? formatRupiah(statisticsData.total_order_price) : ""}</h3>
                  </CardContent>
                </Card>
              </div>
              <div className="w-full sm:w-1/2 md:w-1/2 lg:w-1/2 xl:w-1/2 p-4">
                <Card className="bg-green-50">
                  {/* Card content */}
                  <CardHeader>
                    <CardTitle>Total Rata-rata Harga pesanan </CardTitle>
                    <CardDescription>jumlah rata rata total harga pesanan</CardDescription>
                  </CardHeader>
                  <CardContent>
                    <h3 className=" text-2xl font-semibold tracking-tight">{statisticsData ? formatRupiah(statisticsData.averageOrderPrice) : ""}</h3>
                  </CardContent>
                </Card>
              </div>
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </>
  );
}
