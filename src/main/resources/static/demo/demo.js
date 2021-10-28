Ext.onReady(function () {
  // Ext.tip.QuickTipManager.init();

  var reload = function () {
    userStore.load();
  };

  var userStore = Ext.create('MyExt.Component.SimpleJsonStore', {
    dataUrl: '../demo/searchDemo.do',
    rootFlag: 'data',
    pageSize: 200,
    fields: ['id', 'name', 'comment']
  });

  userStore.on('beforeload', function (store, options) {
    options.params = Ext.apply(options.params || {}, { "simpleSearch": searchForm.getForm().getValues() });
  });


  var searchForm = Ext.create('Ext.form.Panel', {
    region: 'north',
    frame: true,
    height: 80,
    bodyStyle: 'padding:15px 0px 0px 10px',
    fieldDefaults: {
      labelWidth: 30
    },
    defaults: {
      width: 300
    },
    defaultType: 'textfield',
    buttonAlign: 'left',
    items: [{
      fieldLabel: '搜索',
      width: 600,
      emptyText: 'name',
      name: 'simpleSearch',
      enableKeyEvents: true,
      listeners: {
        keypress: function (thiz, e) {
          if (e.getKey() == Ext.EventObject.ENTER) {
            userGrid.getPageToolbar().moveFirst();
          }
        }
      }
    }]
  });

  var userGrid = Ext.create('MyExt.Component.GridPanel', {
    region: 'center',
    title: '用户列表',
    hasInfoBbar: true,
    hasBbar: false,
    store: userStore,
    columns: [{
      dataIndex: 'id',
      header: 'ID',
      width: 320
    }, {
      dataIndex: 'name',
      header: "用户名",
      width: 200
    }, {
      dataIndex: 'comment',
      header: "备注",
      flex: 1
    }],
    tbar: [{
      text: '增加',
      iconCls: 'MyExt-add',
      handler: function () {
        formWindow.changeFormUrlAndShow('../demo/addDemo.do');
      }
    }, {
      text: '修改',
      iconCls: 'MyExt-modify',
      handler: function () {
        var select = MyExt.util.SelectGridModel(userGrid);
        if (!select) {
          return;
        }
        formWindow.changeFormUrlAndShow('../demo/updateDemo.do');
        formWindow.getFormPanel().getForm().loadRecord(select[0]);
      }
    }, {
      text: '删除',
      iconCls: 'MyExt-delete',
      handler: function () {
        var select = MyExt.util.SelectGridModel(userGrid, true);
        if (!select) {
          return;
        }
        MyExt.util.MessageConfirm('是否确定删除', function () {
          MyExt.util.Ajax('../demo/deleteDemo.do', {
            id: select[0].data["id"],
          }, function (data) {
            reload();
            MyExt.Msg.alert('删除成功!');
          });
        });
      }
    }]
  });

  var formWindow = new MyExt.Component.FormWindow({
    title: '操作',
    width: 400,
    height: 200,
    formItems: [{
      name: 'id',
      hidden: true
    }, {
      fieldLabel: '用户名(*)',
      name: 'name',
      allowBlank: false
    }, {
      fieldLabel: '备注(*)',
      name: 'comment',
      allowBlank: true
    }],
    submitBtnFn: function () {
      var form = formWindow.getFormPanel().getForm();
      if (form.isValid()) {
        MyExt.util.Ajax(formWindow.getFormPanel().url, {
          formString: Ext.JSON.encode(form.getValues())
        }, function (data) {
          formWindow.hide();
          reload();
          MyExt.Msg.alert('操作成功!');
        });
      }
    }
  });

  Ext.create('Ext.container.Viewport', {
    layout: 'border',
    items: [searchForm, userGrid]
  });
  reload();

})