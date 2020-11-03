package handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * handler
 *  ==>echoServer接收服务器消息，需要实现ChannelInboundHandler接口，用来定义响应进站的事件方法，
 *  ==>该简单的程序用到少量方法，集成channelInboundHandlerAdapter，提供了ChannelInboundHandler的默认实现
 *   channel
 *
 * @author wangwenjie
 * @date 2020-10-31
 */
//标识一个ChannelHandler可以被多个Channel安全地共享
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 对于每个传入的消息都要调用
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        //消息记录控制台
        System.out.println("==>Server received: " + in.toString(CharsetUtil.UTF_8));
        //接收的消息发送给发送者
        final String reponseMsg = "helloworld";
        System.out.println("==> Server callBack " + reponseMsg);
        ctx.write(reponseMsg);
    }

    /**
     * 当前批量读取的最后一条消息
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //未决消息冲刷到远程节点，并且关闭该channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 读取操作期间，有异常抛出时会调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常栈
        cause.printStackTrace();
        //关闭channel
        ctx.close();
    }
}
