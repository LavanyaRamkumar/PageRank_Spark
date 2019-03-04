/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package org.apache.spark.examples

import org.apache.spark.sql.SparkSession

/**
 * Computes the PageRank of URLs from an input file. Input file should
 * be in format of:
 * URL         neighbor URL
 * URL         neighbor URL
 * URL         neighbor URL
 * ...
 * where URL and their neighbors are separated by space(s).
 *
 * This is an example implementation for learning how to use Spark. For more conventional use,
 * please refer to org.apache.spark.graphx.lib.PageRank
 *
 * Example Usage:
 * {{{
 * bin/run-example SparkPageRank data/mllib/pagerank_data.txt 10
 * }}}
 */
object SparkPageRank {

  def showWarning() {
    System.err.println(
      """WARN: This is a naive implementation of PageRank and is given as an example!
        |Please use the PageRank implementation found in org.apache.spark.graphx.lib.PageRank
        |for more conventional use.
      """.stripMargin)
  }

  def main(args: Array[String]) {
    if (args.length < 1) {
      System.err.println("Usage: SparkPageRank <file> <iter>")
      System.exit(1)
    }

    showWarning()

    val spark = SparkSession
      .builder
      .appName("SparkPageRank")
      .getOrCreate()

    val iters = if (args.length > 1) args(1).toInt else 10
    val lines = spark.read.textFile(args(0)).rdd
    val links = lines.map{ s =>
      val parts = s.split("\\s+")
      (parts(0), parts(1))
    }.distinct().groupByKey().cache()
    var count = 0
	var r=0
	val outer = new Breaks;
	outer.breakable{
	while(count<10)
	{ var f=0
	  var temp = ranks.values.collect
	  val contribs = links.join(ranks).values.flatMap{ case (urls,rank) =>
	  val size = urls.size
	  urls.map(url => (url,rank / size))
	}
	ranks = contribs.reduceByKey(_+_).mapValues(0.15 + 0.85*_)
	var l=ranks.values.collect
	
	var inner = new Breaks;
	inner.breakable
	{
	while(r<l.length)
	{  if(Math.abs(l(0)-temp(0))>=0.03)
	   {
	     inner.break
	   }
	   r=r+1
	}
	}
	if(r==l.length)
	{
	 outer.break
	}
	count=count+1
	}}
	
	val output = ranks.collect()
    	output.foreach(tup => println(s"${tup._1} has rank:  ${tup._2} ."))

    	spark.stop()
	

// scalastyle:on println
