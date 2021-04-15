const d3 = require('d3');
/**
 * 关系力导向图 - d3.js制作   （pc,wap通用）
 * @param id    {string} 父容器id
 * @param configPar      {JSON对象}    配置
 */
export default {
  config: {
    width: 300, // 总画布svg的宽
    height: 300,
    nodes: [],
    links: [],
    isScale: true,      //是否启用缩放平移zoom功能
    scaleExtent: [0.1, 4],  //缩放的比例尺
    chargeStrength: 400,    //万有引力
    collide: 70,        //碰撞力的大小 （节点之间的间距）
    r: 30,      // 头像的半径 [30 - 45]
    linkColor: '#bad4ed',    //链接线默认的颜色
    markerColor: "steelblue",
    rectFill: "blue",
    textFill: "#c2c2c2",
    strokeWidth: 2, // 头像外围包裹的宽度
    svgClick: null
  },
  nodeData: null,
  id: null,
  simulation: null,
  SVG: null,
  defs: null,
  marker: null,
  relMap_g: null,
  links: null,
  edges: null,
  circles: null,
  texts: null,
  markerWidth: 10,

  init(id, configPar) {
    this.id = id
    this.nodeData = null;

    this.config = {
      ...this.config,
      ...configPar
    }
    var _that = this;
    // var timeout;//定时器


    // 1. 创建一个力学模拟器
    this.simulation = d3.forceSimulation(this.config.nodes)
      .force("link", d3.forceLink(this.config.links).id(d => d.id))
      .force("center", d3.forceCenter(this.config.width / 2 + 150, this.config.height / 2))
      .force("charge", d3.forceManyBody().strength(this.config.chargeStrength).distanceMax(100))
      .force("collide", d3.forceCollide(this.config.collide).strength(0.2).iterations(5))
      .alphaDecay(0.2)

    // 2.创建svg标签
    this.SVG = d3.select("#" + id).append("svg")
      .attr("width", this.config.width)
      .attr("height", this.config.height)
      .call(d3.zoom().scaleExtent(this.config.scaleExtent).on("zoom", (event) => {
        if (this.config.isScale) {
          _that.relMap_g.attr("transform", event.transform);
        }
      }))
      .on('click', (e) => {
        if (e.target.tagName != 'circle' && e.target.tagName != 'CIRCLE') {
          this.nodeData = null
        }
        if (this.config.svgClick) { this.config.svgClick(this.nodeData) }
      })
      .on("dblclick.zoom", null);


    this.defs = this.SVG.append('defs');
    // 添加箭头
    this.marker = this.defs
      .append("marker")
      .attr('id', "marker")
      .attr("markerWidth", this.markerWidth)    //marker视窗的宽
      .attr("markerHeight", this.markerWidth)   //marker视窗的高
      .attr("refX", this.config.r + 6 + this.config.strokeWidth) //refX和refY，指的是图形元素和marker连接的位置坐标
      .attr("refY", 4)
      .attr("orient", "auto")     //orient="auto"设置箭头的方向为自动适应线条的方向
      .attr("markerUnits", "userSpaceOnUse")  //marker是否进行缩放 ,默认值是strokeWidth,会缩放
      .append("path")
      .attr("d", "M 0 0 8 4 0 8Z")
      .attr("fill", this.config.markerColor);

    this.setPatterns()


    // 放关系图的容器
    this.relMap_g = this.SVG.append("g")
      .attr("class", "relMap_g")
      .attr("width", this.config.width)
      .attr("height", this.config.height);

    this.gedges = this.relMap_g.append('g').attr('class', 'edges')

    this.setEdges()

    this.setCircles()

    this.simulationTick()
    return this
  },
  // 添加多个头像图片的 <pattern>
  setPatterns() {
    this.defs.selectAll("pattern.patternclass").remove()
    let s = this.defs
      .selectAll("pattern.patternclass")
      .data(this.config.nodes)
    // s.exit().remove()
    let patterns = s.enter().append("pattern")
      .attr("class", "patternclass")
      .attr("id", (d) => {
        return 'avatar' + this.id + d.id;
      })
      .attr("width", "1")
      .attr("height", "1");

    // 姓名的背景
    patterns.append("rect").attr("x", "0").attr("y", 0)
      .attr("width", 2 * this.config.r)
      .attr("height", 2 * this.config.r)
      .attr("fill", (d) => {
        return d.style.fill || this.config.rectFill
      })

    // 姓名
    patterns.append("text")
      .attr("x", this.config.r).attr("y", (1.14 * this.config.r))
      .attr('text-anchor', 'middle')
      .attr("fill", "#fff")
      .style("font-size", this.config.r / 3)
      .text(function (d) {
        return d.name;
      });
  },
  setEdges() {
    let _that = this
    this.gedges.selectAll("g.edge").remove()
    let s = this.gedges
      .selectAll("g.edge")
      .data(this.config.links)
    // s.exit().remove()

    let edges = s.enter().append("g")
      .attr("class", "edge")

    // 5.2 添加线
    edges.append("path").attr("class", "links")
      .attr("id", (d) => {
        return 'path' + _that.id + d.id;
      })
      // this.config.r + this.markerWidth 圆半径加箭头宽度
      .attr("d", d => { return "M" + (_that.config.r + _that.markerWidth) + "," + 0 + " L" + getDis(d.source, d.target) + ",0"; })
      .style("marker-end", "url(#marker)")
      .attr('stroke', (d) => {
        var str = d.color ? d.color : _that.config.linkColor;
        return str;
      });

    // 文本标签  坐标（x,y）代表 文本的左下角的点
    edges.append("text").attr("class", "texts")
      .attr('y', -2)
      .attr('fill', this.config.textFill)
      .attr("text-anchor", "end")
      .style("cursor", "pointer")
      .style("font-size", 8).text(d => { return d.type })
      .on('mouseover', function () {
        d3.select(this).attr('fill', '#ffffff').style("font-size", 10);
      })
      .on('mouseout', function () {
        d3.select(this).attr('fill', _that.config.textFill).style("font-size", 8);
      })
      .on('click', () => {
        this.config.textClick && this.config.textClick()
      })
      // .append('textPath').attr('yxlink:href', function(d) {
      //   return ("url(#path" + _that.id + d.id + ")")
      // })

    this.edges = this.gedges.selectAll("g.edge")
    this.links = this.gedges.selectAll("path.links")
    this.texts = this.gedges.selectAll("text.texts")
  },
  // 关系图添加用于显示头像的节点
  setCircles() {
    let _that = this
    this.relMap_g.selectAll("circle.circleclass").remove()

    let s = this.relMap_g
      .selectAll("circle.circleclass")
      .data(this.config.nodes)
    // s.exit().remove()

    s.enter().append("circle")
      .attr("class", (d) => {
        return "circleclass i" + d.typeKey
      })
      .style("cursor", "pointer")
      .attr("fill", function (d) {
        return ("url(#avatar" + _that.id + d.id + ")");
      })
      .attr("stroke", function (d) {
        return d.style.stroke || "#ccf1fc";
      })
      .attr("stroke-width", this.config.strokeWidth)
      .attr("r", this.config.r)
      .on('mouseover', function () {
        d3.select(this).attr('stroke-width', '4');
        d3.select(this).attr('stroke', '#a3e5f9');
      })
      .on('mouseout', function () {
        d3.select(this).attr('stroke-width', _that.config.strokeWidth);
        d3.select(this).attr('stroke', function (d) {
          return d.style.stroke || "#ccf1fc";
        });
      })
      .on('click', function (event, d) {
        _that.nodeData = d
      })
      .on("dblclick", function (event, d) {
        _that.highlightObject(d)
      })
      .call(drag(this.simulation));

      this.circles = this.relMap_g.selectAll("circle.circleclass")
  },
  simulationTick() {
    let offset = (this.config.r + this.markerWidth)
    this.simulation.on("tick", () => {
      // 修改每条容器edge的位置
      this.edges.attr("transform", function (d) {
        return getTransform(d.source, d.target, getDis(d.source, d.target))
      });

      // 修改每条线link位置
      this.links.attr("d", d => { return "M" + (this.config.r + this.markerWidth) + "," + 0 + " L" + getDis(d.source, d.target) + ",0"; })

      // 修改线中关系文字text的位置 及 文字的反正
      this.texts
        .attr("x", function (d) {
          if (d.target.x > d.source.x) {
            return getDis(d.source, d.target) - offset
          } else  {
            return offset
          }
          // return d.target.x - d.source.x + 40
        })
        // .attr("x", function (d) {
        //   return getDis(d.source, d.target) / 2
        // })
        .attr("text-anchor", function (d) {
          if (d.source.x > d.target.x) {
            return 'start'
          } else  {
            return 'end'
          }
          // return d.target.x - d.source.x + 40
        })
        // .attr("x", this.config.r + this.markerWidth)
        .attr("transform", function (d) {
          // 更新文本反正
          if (d.target.x < d.source.x) {
            var x = getDis(d.source, d.target) / 2;
            return 'rotate(180 ' + x + ' ' + 0 + ')';
          } else {
            return 'rotate(0)';
          }
        });

      // 修改节点的位置
      this.circles
        .attr("cx", function (d) {
          return d.x;
        })
        .attr("cy", function (d) {
          return d.y;
        })
    });
  },
  /**
   * 更新视图
   */
  update(configPar) {
    this.config = {
      ...this.config,
      ...configPar
    }
    this.simulation.nodes(this.config.nodes)
      .force("link", d3.forceLink(this.config.links).id(d => d.id))
      .alphaTarget(0.2)
      .restart()
      setTimeout(()=> {
        this.simulation.alphaTarget(0);
      }, 200)

    this.setPatterns()

    this.setEdges()

    this.setCircles()

  },
  search(txt, attrs) {
    if (!txt) {
      return []
    }
    let res = []
    this.config.nodes.forEach(v => {
      for (let i = 0; i < attrs.length; i++) {
        const attr = attrs[i];
        if (v[attr] && v[attr].indexOf(txt) > -1) {
          res.push(v)
          return
        }
      }
    })
    return res
  },
  highlighted: null,
  dependsNode: [],
  dependsLinkAndText: [],
  highlightObject(obj) {
    var _that = this;
    if (obj) {
      var objIndex = obj.index;
      _that.dependsNode = _that.dependsNode.concat([objIndex]);
      _that.dependsLinkAndText = _that.dependsLinkAndText.concat([objIndex]);
      _that.config.links.forEach(function (lkItem) {
        if (objIndex == lkItem['source']['index']) {
          _that.dependsNode = _that.dependsNode.concat([lkItem.target.index]);
        } else if (objIndex == lkItem['target']['index']) {
          _that.dependsNode = _that.dependsNode.concat([lkItem.source.index]);
        }
      });

      // 隐藏节点
      _that.SVG.selectAll('circle').filter(function (d) {
        return (_that.dependsNode.indexOf(d.index) == -1);
      }).transition().style('opacity', 0.1);
      // 隐藏线
      _that.SVG.selectAll('.edge').filter(function (d) {
        // return true;
        return ((_that.dependsLinkAndText.indexOf(d.source.index) == -1) && (_that.dependsLinkAndText.indexOf(d.target.index) == -1))
      }).transition().style('opacity', 0.1);

    } else {
      // 恢复隐藏的线
      _that.SVG.selectAll('circle').transition().style('opacity', 1);
      // 恢复隐藏的线
      _that.SVG.selectAll('.edge').transition().style('opacity', 1);
      _that.highlighted = null
      _that.dependsNode = []
      _that.dependsLinkAndText = []
    }
  }

}

function drag(simulation) {
  function dragstarted(event) {
    if (!event.active) simulation.alphaTarget(0.1).restart();
    event.subject.fx = event.subject.x;
    event.subject.fy = event.subject.y;
  }

  function dragged(event) {
    event.subject.fx = event.x;
    event.subject.fy = event.y;
  }

  function dragended(event) {
    if (!event.active) simulation.alphaTarget(0);
    event.subject.fx = null;
    event.subject.fy = null;
  }

  return d3.drag()
    .on("start", dragstarted)
    .on("drag", dragged)
    .on("end", dragended);
}

// 求两点间的距离
function getDis(s, t) {
  return Math.sqrt((s.x - t.x) * (s.x - t.x) + (s.y - t.y) * (s.y - t.y));
}

// 求元素移动到目标位置所需要的 transform 属性值
function getTransform(source, target, _dis) {
  var r;
  if (target.x > source.x) {
    if (target.y > source.y) {
      r = Math.asin((target.y - source.y) / _dis)
    } else {
      r = Math.asin((source.y - target.y) / _dis);
      r = -r;
    }
  } else {
    if (target.y > source.y) {
      r = Math.asin((target.y - source.y) / _dis);
      r = Math.PI - r;
    } else {
      r = Math.asin((source.y - target.y) / _dis);
      r -= Math.PI;
    }
  }
  r = r * (180 / Math.PI);
  return "translate(" + source.x + "," + source.y + ")rotate(" + r + ")";
}