package com.ymatou;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;


public class PoolFull {
	/**
	 * 获取最小公倍数
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public BigInteger getMinD(BigInteger biga, BigInteger bigb) {
		if (biga == null || bigb == null) {
			return null;
		}
		return biga.multiply(bigb).divide(biga.gcd(bigb)).abs();
	}

	/**
	 * 获取多个数字最小公倍数
	 * 
	 * @param pools
	 * @return
	 */
	public BigInteger getListMinD(List<Integer> pools) {
		if (pools == null || pools.isEmpty() || pools.contains(null)) {
			return null;
		}
		BigInteger MinD = new BigInteger(pools.get(0).toString());
		for (Integer i : pools) {
			MinD = getMinD(MinD, new BigInteger(i.toString()));
		}
		return MinD;
	}

	/**
	 * 获取增量list
	 * 
	 * @param pools
	 * @return
	 */
	public List<Long> getIncreList(List<Integer> pools) {
		BigInteger MinD = getListMinD(pools);
		List<Long> increList = new ArrayList<Long>();
		pools.forEach(data -> {
			increList.add(MinD.divide(new BigInteger(data.toString())).longValue());
		});
		return increList;
	}

	/**
	 * 添加值 小于0则为0
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public BigInteger addPool(BigInteger a, BigInteger b) {
		if (a.add(b).compareTo(new BigInteger("0"))>0) {
			return a.add(b);
		} else {
			return new BigInteger("0");
		}
	}

	/**
	 * @param IncreList
	 * @return
	 */
	public Long getMaxOneIncre(List<Long> IncreList) {
		Long maxincre = 0l;
		Long temp = 0l;
		for (Long in : IncreList) {
			temp = temp + in.longValue();
			if(temp<0){
				temp=0l;
			}
			if (temp > maxincre) {
				maxincre = temp;
			}
		}
		return maxincre;
	}
	/**
	 * 残量 10 -15 1 -> 1
	 * 
	 * @param IncreList
	 * @return
	 */
	public BigInteger getRemain(List<Long> IncreList) {
		BigInteger temp = new BigInteger("0");
		for (Long in : IncreList) {
			temp = addPool(temp, new BigInteger(in.toString()));
		}
		return temp;
	}

	/**
	 * @param pools
	 * @return <0代表永远不成功
	 */
	public Double getTime(List<Integer> pools) {
		if(pools==null||pools.isEmpty()||pools.contains(null)){
			System.err.println("包含null值,failed");
			System.out.println();
			return -1d;
		}
		// 总水位
	    BigInteger MinD = getListMinD(pools);
//		System.out.println("MinD:"+MinD);
		// 每次的变化量
		List<Long> IncreList = getIncreList(pools);
//		System.out.println("IncreList:"+IncreList);
		// 经过的时间
		Double time = 0d;
		// 当前水位
		BigInteger poolw = new BigInteger("0");
		// 当前的水管编号
		Integer poffset = 0;
		// 总共的池子数量
		Integer allsize = IncreList.size();
		// 每次循环的增量
		Long oincre = IncreList.stream().mapToLong((num) -> num.longValue()).summaryStatistics().getSum();
//		System.out.println("oincre:"+oincre);
		// 负增长时 一次内可能达到的最大量
		Long maxincre = getMaxOneIncre(IncreList);
//		System.out.println("maxincre:"+maxincre);
		// 如果负增长
		if (oincre <= 0 && maxincre < MinD.longValue()) {
			//第一次已经执行过了
			time = 0d + allsize;
			poolw = getRemain(IncreList);
			// 这里尝试第一次残量+本次能否达到
			
			if (poolw.add(new BigInteger(maxincre.toString())).compareTo(MinD)>=0) {
				for (Long incre : IncreList) {
					poolw = addPool(poolw,new BigInteger(incre.toString()));
					if (poolw.compareTo(MinD)>=0) {
						// 多出来的水消耗的时间 h
						Double last =new BigDecimal(poolw.subtract(MinD).toString()).divide(new BigDecimal(incre.toString()),2, BigDecimal.ROUND_HALF_UP).doubleValue();
						time = time + 1 - last;
						return time;
					}
					time++;
				}
				//跑了2圈没能达到
				return -1d;
			} else {
				//肯定到不了 返回-1
				return -1d;
			}
		}
		// 正增长进入循环
		else {
			boolean flag = true;
			while (flag) {
				poolw = addPool(poolw, new BigInteger(IncreList.get(poffset).toString()));
//				System.out.println("poolw:"+poolw);
				if (poolw.compareTo(MinD)>0) {
					flag = false;
					// 多出来的水消耗的时间 h
					Double last=new BigDecimal(poolw.subtract(MinD).toString()).divide(new BigDecimal(IncreList.get(poffset).toString()),2, BigDecimal.ROUND_HALF_UP).doubleValue();
//					System.out.println("last:"+last);
//					Double last = (poolw - MinD) / IncreList.get(poffset).doubleValue();
					time = time + 1 - last;
				}else if(poolw.compareTo(MinD)==0){
					flag = false;
					time = time + 1 ;
				} else {
					time = time + 1;
					poffset++;
					if (poffset >= allsize) {
						poffset = 0;
					}
				}
			}
		}
		return time;
	}
	public static boolean verifyEquals(Object expected, Object actual, String Description) {
    	boolean vflag=true;
        if (expected == null || actual == null) {
            if (expected == actual) {
            	System.out.println("Success:"+Description + "\t期望：" + expected + " 实际：" + actual);
            } else {
                vflag=false;
                System.out.println("Failed:"+Description + "\t期望：" + expected + " 实际：" + actual);
            }
        } else if (expected.equals(actual)) {
        	System.out.println("Success:"+Description + "\t期望：" + expected + " 实际：" + actual);
        } else {
            vflag=false;
            System.out.println("Failed:"+Description + "\t期望：" + expected + " 实际：" + actual);
        }
        
        return vflag;
    }
	
	public static void main(String args[]) {
		PoolFull PoolFull = new PoolFull();
//		List<Integer> pools = Arrays.asList(999, -1000, 1999, -2000,2001,-2002,2003,-2004,2005,-2006);
		List<Integer> pools = Arrays.asList(3, -4, 5, -6);
		//TODO 该算法无法处理小数/可以以时间为单位优化返回/大数据进行计算时算法优化/other...
//		System.out.println("24.9:"+PoolFull.getTime(pools));
		PoolFull.verifyEquals(24.9d, PoolFull.getTime(pools), "小数结果验证");
		pools = Arrays.asList(2);
		PoolFull.verifyEquals(2d, PoolFull.getTime(pools), "最简单场景验证");
		pools = Arrays.asList(-1);
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "负增长不满验证");
		pools = Arrays.asList(-1,2,3,-100);
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "多参数负增长不满验证");
		pools = Arrays.asList(2,-2,3,-3);
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "不增长验证");
		pools = Arrays.asList(4,4,4,-1,4);
		PoolFull.verifyEquals(8d, PoolFull.getTime(pools), "第二次满验证");
		pools = Arrays.asList(4,4,4,-1,-1,4);
		PoolFull.verifyEquals(9d, PoolFull.getTime(pools), "第二次满验证-每次总的放水量负增长");
		pools = Arrays.asList(21, -20, 9, -10);
		PoolFull.verifyEquals(410.99d, PoolFull.getTime(pools), "结果保留2位验证-四舍五入");
		pools = Arrays.asList(3,5,4,-7,6,-2,1);
		PoolFull.verifyEquals(6.69d, PoolFull.getTime(pools), "结果保留2位验证-四舍五入");
		pools = Arrays.asList(999, -1000, 1999, -2000);
		PoolFull.verifyEquals(3193921d, PoolFull.getTime(pools), "Int溢出验证");
		pools = Arrays.asList(999, -1000, 1999, -2000,2001,-2002,2003,-2004);
		PoolFull.verifyEquals(4567177d, PoolFull.getTime(pools), "Long溢出验证");
		pools = new ArrayList<>();
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "isEmpty验证");
		pools.add(null);
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "null验证");
		pools = Arrays.asList(null,1);
		PoolFull.verifyEquals(-1d, PoolFull.getTime(pools), "包含null验证");
		pools = Arrays.asList(-1,2,3,6);
		PoolFull.verifyEquals(4d, PoolFull.getTime(pools), "第一次先放再满验证");
	}
}
