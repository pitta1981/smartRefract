/*

* Complex.java

 *

 * JPick is a free tool for picking first arrivals from SEG-2

 * seismic data.  JPick uses the JFreeChart library from 
http://www.jfree.org/jfreechart/

 */


/**

*

* Copyright 2004, Will Brunner

* will@larkinsoap.com

*

* 117 grove school road

* Brooktondale NY 14817

*

*This file is part of the JPick first arrival picker.


JPick is free software; you can redistribute it and/or modify

it under the terms of the GNU General Public License as published by

the Free Software Foundation; either version 2 of the License, or

(at your option) any later version.


JPick is distributed in the hope that it will be useful,

but WITHOUT ANY WARRANTY; without even the implied warranty of

MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the

GNU General Public License for more details.


You should have received a copy of the GNU General Public License

along with JPick; if not, write to the Free Software

Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

*/


 
package org.myorg.myapi;
 public class Complex implements Cloneable{
	private double real,
		imaginary;
	Complex(){
		real = 0;
		imaginary = 0;
	}
	Complex(double r, double i){
		real = r;
		imaginary = i;
	}
	public double getReal(){
		return real;
	}
	public double getImaginary(){
		return imaginary;
	}
	public void setReal(double r){
		real = r;
	}
	public void setImaginary(double i){
		imaginary = i;
	}
	public Complex plus(Complex b){
		Complex c = new Complex(this.getReal()+b.getReal(), this.getImaginary()+b.getImaginary());
		return c;
	}
	public Complex plus(int b){
		Complex c = new Complex(this.getReal()+b, this.getImaginary());
		return c;
	}
	public Complex plus(double b){
		Complex c = new Complex(this.getReal()+b, this.getImaginary());
		return c;
	}
	public Complex minus(Complex b){
		Complex c = new Complex(this.getReal()-b.getReal(), this.getImaginary()-b.getImaginary());
		return c;
	}
	public Complex minus(int b){
		Complex c = new Complex(this.getReal()-b, this.getImaginary());
		return c;
	}
	public Complex minus(double b){
		Complex c = new Complex(this.getReal()-b, this.getImaginary());
		return c;
	}
	public Complex times(Complex b){
		double r = this.getReal()*b.getReal()-this.getImaginary()*b.getImaginary();
		double i = this.getReal()*b.getImaginary()+this.getImaginary()*b.getReal();
		Complex c = new Complex(r, i);
		return c;
	}
	public Complex times(int b){
		double r = this.getReal()*b;
		double i = this.getImaginary()*b;
		Complex c = new Complex(r, i);
		return c;
	}
	public Complex times(double b){
		double r = this.getReal()*b;
		double i = this.getImaginary()*b;
		Complex c = new Complex(r, i);
		return c;
	}
	public Complex conjugate(){
		Complex c = new Complex(this.getReal(),-this.getImaginary());
		return c;
	}
	public Object clone(){
		Complex c = new Complex(this.getReal(), this.getImaginary());
		return c;
	}
		
}
