package es.upm.fi.dia.oeg.obdi.wrapper.r2rml.fusiontables.engine

import com.google.api.services.fusiontables.model.Table;

class View {
  def header(name : String) = {
    System.out.println();
    System.out.println("================== " + name + " ==================");
    System.out.println();
  }

  def show(table : Table) = {
    System.out.println("id: " + table.getTableId());
    System.out.println("name: " + table.getName());
    System.out.println("description: " + table.getDescription());
    System.out.println("attribution: " + table.getAttribution());
    System.out.println("attribution link: " + table.getAttributionLink());
    System.out.println("kind: " + table.getKind());

  }

  def separator() = {
    System.out.println();
    System.out.println("------------------------------------------------------");
    System.out.println();
  }
}

object View {
  val view = new View();
  view;
}