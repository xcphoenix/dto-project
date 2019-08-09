<template>
  <div>
    <div>
        <button class="default"><slot></slot></button>
      </div>
</div>
</template>
<script>
export default{
    name:"comBtn"
}
</script>
<style>
.default {
  width: 80%;
  margin-left: 10%;
  margin-top: 80px;
  padding: 18px 20px;
  vertical-align: middle;
  background-color: rgb(25, 137, 250);
  color: #fff;
  border: 1px solid rgb(25, 137, 250);
  border-radius: 4px;
  cursor: pointer;
}
</style>