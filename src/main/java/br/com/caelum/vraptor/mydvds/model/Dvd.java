/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package br.com.caelum.vraptor.mydvds.model;

import java.math.BigInteger;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Dvd entity.
 * 
 * Represents the table DVD from the database.
 * 
 * A persisted object of this class represents a record in the database.
 */
@Entity
public class Dvd {

	/*
	 * Primary key.
	 */
	
	@Id
	@GeneratedValue
	private BigInteger id;

	private String title;

	private String description;

	private String owner;

	// dvd to user mapping
	@OneToMany(mappedBy = "dvd")
	private Set<DvdCopy> copies;

	@Enumerated(EnumType.STRING)
	private DvdType type;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public DvdType getType() {
		return type;
	}

	public void setType(DvdType type) {
		this.type = type;
	}

	public Set<DvdCopy> getCopies() {
		return copies;
	}

	public void setCopies(Set<DvdCopy> copies) {
		this.copies = copies;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Dvd other = (Dvd) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	public void setId1(String id) {
		this.id = new BigInteger(id,16);
	}
	
	public static void main(String[] args) {
		
		BigInteger bigInteger = new BigInteger("b2b76c0227fffa4a5ccc9b00",16);
		System.out.println(bigInteger.toString(16));
		System.out.println(bigInteger.longValue());
//		Long valueOf = Long.valueOf("b2b76c0227fffa4",16);
//		System.out.println(valueOf);
	}

	public String getId1() {
		return this.id.toString(16);
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwner() {
		return owner;
	}

}
