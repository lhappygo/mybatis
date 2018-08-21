/**
 *    Copyright 2009-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 *   statementHandler
 *      数据库会话处理器
 *
 *        作用：专门处理数据库会话。（进行预编译并且调用parameterHandler的setParameters()方法设置参数
 *
 *        类型有三种：simpleStatementHandler、prepareStatementHandler、callableStatementHandler，分别对应
 *      executor的三种执行器(simple、reuse、batch)。
 *
 *      a、statementHandler的生成是由configuration方法中的newStatementHandler()方法生成的，但是正在创建的是
 *      statementHandler接口的实现类routingStatementHandler对象。
 *
 *      b、routingStatementHandler的通过适配器模式找到对应的（上下文）statementHandler执行的，并且有simpleStatementHandler、
 *      prepareStatementHandler、callableStatementHandler，分别对应executor的三种执行器(simple、reuse、batch)
 *
 *      c、baseStatementHandler中重写prepare()方法，instantiateStatement()方法完成预编译，之后设置一些基础配置（获取最大行数，超时)
 *
 *      d、init
 * @author Clinton Begin
 */
public interface StatementHandler {

  /**
   * executor 执行器会先调用statementHandler.prepare()方法预编译sql语句
   * @param connection
   * @param transactionTimeout
   * @return
   * @throws SQLException
   */
  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;

  /**
   * 然后调用parameterize()方法(实际上启用ParameterHandler设置参数)
   * @param statement
   * @throws SQLException
   */
  void parameterize(Statement statement)
      throws SQLException;

  void batch(Statement statement)
      throws SQLException;

  int update(Statement statement)
      throws SQLException;

  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;

  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;

  BoundSql getBoundSql();

  ParameterHandler getParameterHandler();

}
